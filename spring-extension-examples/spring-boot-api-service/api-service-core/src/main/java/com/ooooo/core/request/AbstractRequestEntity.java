package com.ooooo.core.request;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ooooo.core.annotation.APIMapping;
import com.ooooo.core.annotation.APIService;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.context.APIServiceContext;
import com.ooooo.core.exception.APIException;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.core.util.ParamUtil;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;
import java.util.Map;

import static com.ooooo.core.constants.CounterConstants.APISERVICE_CONFIG_KEY;
import static com.ooooo.core.constants.CounterConstants.FUNCTION_NAME;
import static com.ooooo.core.constants.CounterConstants.GENERIC_SERVICE_URL_KEY;
import static com.ooooo.core.constants.CounterConstants.GENERIC_SERVICE_URL_VALUE;
import static com.ooooo.core.constants.CounterConstants.REQUEST_ID_KEY;
import static com.ooooo.core.constants.CounterConstants.TEMPLATE_ID_KEY;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/8/25 09:58
 */
public abstract class AbstractRequestEntity<T> {
	
	@Getter
	protected MethodInvocation invocation;
	@Getter
	protected APIServiceConfig apiServiceConfig;
	@Getter
	protected String requestId;
	@Getter
	protected String templateId;
	@Getter
	protected String url;
	@Getter
	protected String apiMappingNote;
	@Getter
	protected T body;
	@Getter
	protected ServiceType serviceType;
	
	/**
	 * invocation 中的所有参数
	 */
	@Getter
	@Setter
	protected Map<String, Object> params;
	
	public AbstractRequestEntity(MethodInvocation invocation) {
		this.invocation = invocation;
		this.apiServiceConfig = (APIServiceConfig) APIServiceContext.getAttribute(invocation, APISERVICE_CONFIG_KEY);
		this.serviceType = apiServiceConfig.getServiceType();
		this.params = buildParams();
		this.url = buildUrl();
		this.apiMappingNote = buildApiMappingNote();
		this.requestId = buildRequestId();
		this.templateId = buildTemplateId();
		this.body = buildBody();
	}
	
	
	/**
	 * 根据策略选择templateId
	 * <p>1. method</p>
	 * <p>2. class</p>
	 * <p>3. params</p>
	 * <p>4. CompositePropertySources</p>
	 *
	 * @return
	 */
	protected String buildTemplateId() {
		// method
		Method method = invocation.getMethod();
		APIMapping methodMapping = AnnotationUtils.getAnnotation(method, APIMapping.class);
		if (methodMapping != null && StringUtils.isNotBlank(methodMapping.templateId())) {
			return methodMapping.templateId();
		}
		
		// class
		APIService apiService = AnnotationUtils.getAnnotation(method.getDeclaringClass(), APIService.class);
		if (apiService != null && StringUtils.isNotBlank(apiService.templateId())) {
			return apiService.templateId();
		}
		
		// params
		if (this.params.containsKey(TEMPLATE_ID_KEY)) {
			return (String) this.params.remove(TEMPLATE_ID_KEY);
		}
		
		// CompositePropertySources
		//if (apiServiceConfig != null) {
		//	CompositePropertySources sources = apiServiceConfig.getApplicationContext().getBean(CompositePropertySources.class);
		//
		//	//  TEMPLATE_ID_KEY
		//	if (StringUtils.isNotBlank(sources.getProperty(TEMPLATE_ID_KEY))) {
		//		return sources.getProperty(TEMPLATE_ID_KEY);
		//	}
		//
		//}
		
		return null;
	}
	
	protected String buildRequestId() {
		if (params.containsKey(REQUEST_ID_KEY)) {
			return (String) params.remove(REQUEST_ID_KEY);
		}
		return RandomStringUtils.randomAlphabetic(6);
	}
	
	
	protected String buildApiMappingNote() {
		if (params.containsKey(FUNCTION_NAME)) {
			return (String) params.get(FUNCTION_NAME);
		}
		Method method = invocation.getMethod();
		APIMapping methodMapping = AnnotationUtils.getAnnotation(method, APIMapping.class);
		return methodMapping.note() == null ? "" : methodMapping.note();
	}
	
	protected Map<String, Object> buildParams() {
		return ParamUtil.toMap(invocation.getArguments());
	}
	
	protected T buildBody() {
		// 假定body的类型和params 一样
		return JSON.parseObject(JSON.toJSONString(params), new TypeReference<T>() {});
	}
	
	/**
	 * 通用获取url的方法
	 *
	 * @return
	 */
	protected String buildUrl() {
		Method method = invocation.getMethod();
		APIMapping methodMapping = AnnotationUtils.getAnnotation(method, APIMapping.class);
		String url = methodMapping.value();
		
		if (url == null || GENERIC_SERVICE_URL_VALUE.equals(url)) {
			url = (String) getParams().remove(GENERIC_SERVICE_URL_KEY);
			if (url == null) {
				throw new APIException("泛化调用时，必须有@APIMapping的value");
			}
		}
		return url;
	}
	
}
