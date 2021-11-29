package com.ooooo.core.request;

import com.ooooo.core.annotation.ServiceTypeAware;
import com.ooooo.core.constants.CounterConstants;
import com.ooooo.core.context.APIServiceContext;
import com.ooooo.core.proxy.APIServiceConfig;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author leizhijie
 * @since 2021/8/25 09:55
 */
public interface IRequestEntityProcessor extends ServiceTypeAware {
	
	
	default void invokeBefore(MethodInvocation invocation, APIServiceConfig serviceConfig) {
		APIServiceContext.setAttribute(invocation, CounterConstants.APISERVICE_CONFIG_KEY, serviceConfig);
		AbstractRequestEntity<?> requestEntity = (AbstractRequestEntity<?>) APIServiceContext.getAttribute(invocation, CounterConstants.REQUEST_ENTITY_KEY);
		if (requestEntity != null) {
			return;
		}
		
		requestEntity = doProcoess(invocation);
		APIServiceContext.setAttribute(invocation, CounterConstants.REQUEST_ENTITY_KEY, requestEntity);
	}
	
	default void invokeAfter(MethodInvocation invocation, APIServiceConfig serviceConfig) {
		APIServiceContext.setAttribute(invocation, CounterConstants.REQUEST_ENTITY_KEY, null);
		
	}
	
	AbstractRequestEntity<?> doProcoess(MethodInvocation invocation);
	
}
