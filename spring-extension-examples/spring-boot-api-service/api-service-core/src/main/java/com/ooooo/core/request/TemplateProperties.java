package com.ooooo.core.request;

import com.alibaba.fastjson.JSON;
import com.ooooo.core.constants.CounterConstants;
import com.ooooo.core.interceptor.TraceServiceMethodInterceptor;
import com.ooooo.core.service.MockService;
import com.ooooo.core.service.TraceService;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ClassUtils;


/**
 * template 通用属性
 */
@Data
@NoArgsConstructor
public class TemplateProperties {
	
	/**
	 * @see ITemplate#getId()
	 */
	private String id = CounterConstants.TEMPLATE_ID_DEFAULT_VALUE;
	
	/**
	 * @see ITemplate
	 */
	private String templateClassName;
	
	private String desc;
	
	private boolean debug = false;
	
	/**
	 * @see TraceServiceMethodInterceptor
	 * @see TraceService
	 */
	private boolean trace = false;
	
	/**
	 * @see MockService
	 */
	private boolean mock = false;
	
	/**
	 * 额外的配置，会映射到 templateConfigClass
	 *
	 * @see ITemplate
	 */
	private Map<String, Object> config = new HashMap<>();
	
	public Class<?> resolveTemplateClass() {
		if (templateClassName == null) {
			throw new IllegalArgumentException("property ['" + templateClassName + "'] of TemplateProperties is null");
		}
		
		return ClassUtils.resolveClassName(templateClassName, null);
	}
	
	public <T> T resolveTemplateConfig(Class<T> clazz) {
		T t = JSON.parseObject(JSON.toJSONString(getConfig()), clazz);
		
		return t;
	}
}