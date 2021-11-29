package com.ooooo.core.interceptor;

import com.ooooo.core.annotation.IAPIService;
import com.ooooo.core.proxy.APIServiceConfig;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopContext;

/**
 * 这个拦截器会暴露bean的serviceConfig
 *
 * @author leizhijie
 * @see APIServiceConfig
 * @since 2021/3/23 14:08
 */
public abstract class APIServiceConfigMethodInterceptor implements MethodInterceptor {
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		Object currentProxy = AopContext.currentProxy();
		if (currentProxy instanceof IAPIService) {
			IAPIService service = (IAPIService) currentProxy;
			APIServiceConfig serviceConfig = service.getAPIServiceConfig();
			if (support(serviceConfig)) {
				return doInvoke(invocation, serviceConfig);
			}
		}
		return invocation.proceed();
	}
	
	protected abstract Object doInvoke(MethodInvocation invocation, APIServiceConfig serviceConfig) throws Throwable;
	
	protected boolean support(APIServiceConfig serviceConfig) {
		return true;
	}
	
}
