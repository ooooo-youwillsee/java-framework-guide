package com.ooooo.core.interceptor;

import com.ooooo.core.constants.InterceptorType;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.exception.APIException;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.core.request.AbstractRequestEntity;
import com.ooooo.core.request.IRequestEntityProcessor;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/**
 * @author leizhijie
 * @see AbstractRequestEntity
 * @see InterceptorType
 * @see APIServiceConfigMethodInterceptor
 * @since 2021/8/25 10:05
 */
public class RequestEntityProcessMethodInterceptor extends APIServiceConfigMethodInterceptor implements Ordered {
	
	private Map<ServiceType, IRequestEntityProcessor> processorMap;
	
	public RequestEntityProcessMethodInterceptor(List<IRequestEntityProcessor> processors) {
		if (processors == null) {
			return;
		}
		processorMap = processors.stream().collect(Collectors.toMap(IRequestEntityProcessor::getServiceType, v -> v));
	}
	
	@Override
	protected Object doInvoke(MethodInvocation invocation, APIServiceConfig serviceConfig) throws Throwable {
		ServiceType serviceType = serviceConfig.getServiceType();
		IRequestEntityProcessor requestEntityProcessor = processorMap.get(serviceType);
		if (requestEntityProcessor == null) {
			throw new APIException("serviceType['" + serviceType.name() + "'] 没有对应的 IRequestEntityProcessor");
		}
		
		requestEntityProcessor.invokeBefore(invocation, serviceConfig);
		Object proceed = null;
		try {
			proceed = invocation.proceed();
		} finally {
			requestEntityProcessor.invokeAfter(invocation, serviceConfig);
		}
		
		return proceed;
	}
	
	@Override
	protected boolean support(APIServiceConfig serviceConfig) {
		return processorMap != null && processorMap.containsKey(serviceConfig.getServiceType());
	}
	
	@Override
	public int getOrder() {
		return InterceptorType.REQUEST_ENTITY_PROCESSOR.getOrder();
	}
}
