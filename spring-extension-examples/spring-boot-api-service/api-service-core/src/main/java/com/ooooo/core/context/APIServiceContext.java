package com.ooooo.core.context;

import com.ooooo.core.beans.AbstractTemplateBeanDefintionRegistrar;
import com.ooooo.core.constants.CounterConstants;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.TemplateProperties;
import java.util.Map;
import java.util.Map.Entry;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.context.ApplicationContext;

/**
 * @author leizhijie
 * @since 2021/3/18 16:47
 */
public class APIServiceContext {
	
	public static Object getAttribute(MethodInvocation invocation, String key) {
		if (!(invocation instanceof ReflectiveMethodInvocation)) {
			return null;
		}
		ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;
		return reflectiveMethodInvocation.getUserAttribute(key);
	}
	
	
	public static void setAttribute(MethodInvocation invocation, String key, Object value) {
		if (!(invocation instanceof ReflectiveMethodInvocation)) {
			return;
		}
		ReflectiveMethodInvocation reflectiveMethodInvocation = (ReflectiveMethodInvocation) invocation;
		reflectiveMethodInvocation.setUserAttribute(key, value);
	}
	
	public static AbstractRequestEntity<?> getRequestEntity(MethodInvocation methodInvocation) {
		AbstractRequestEntity<?> request = (AbstractRequestEntity<?>) getAttribute(methodInvocation, CounterConstants.REQUEST_ENTITY_KEY);
		return request;
	}
	
	public static TemplateProperties getTemplateProperties(MethodInvocation methodInvocation) {
		AbstractRequestEntity<?> request = getRequestEntity(methodInvocation);
		if (request == null) {
			return null;
		}
		
		String templateId = request.getTemplateId();
		APIServiceConfig apiServiceConfig = request.getApiServiceConfig();
		if (templateId == null || apiServiceConfig == null) {
			return null;
		}
		
		ServiceType serviceType = request.getServiceType();
		ApplicationContext applicationContext = apiServiceConfig.getApplicationContext();
		TemplateProperties foundTemplateProperties = null;
		
		Map<String, AbstractTemplateBeanDefintionRegistrar> beans = applicationContext.getBeansOfType(AbstractTemplateBeanDefintionRegistrar.class);
		for (Entry<String, AbstractTemplateBeanDefintionRegistrar> entry : beans.entrySet()) {
			AbstractTemplateBeanDefintionRegistrar templateBeanDefintionRegistrar = entry.getValue();
			if (templateBeanDefintionRegistrar.getSerivceType().equals(serviceType)) {
				foundTemplateProperties = templateBeanDefintionRegistrar.getRegisteredTemplatePropertiesMap().get(templateId);
				break;
			}
		}
		
		return foundTemplateProperties;
	}
}
