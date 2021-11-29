package com.ooooo.core.interceptor;

import com.ooooo.core.constants.InterceptorType;
import com.ooooo.core.context.APIServiceContext;
import com.ooooo.core.proxy.APIServiceConfig;
import com.ooooo.core.request.TemplateProperties;
import com.ooooo.core.service.TraceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

/**
 * @author leizhijie
 * @since 2021/8/26 09:28
 */
@Slf4j
@AllArgsConstructor
public class TraceServiceMethodInterceptor extends APIServiceConfigMethodInterceptor implements Ordered {
	
	private TraceService traceService;
	
	@Override
	protected Object doInvoke(MethodInvocation invocation, APIServiceConfig serviceConfig) throws Throwable {
		boolean enabled = traceService != null;
		if (enabled) {
			TemplateProperties templateProperties = APIServiceContext.getTemplateProperties(invocation);
			enabled = templateProperties != null && templateProperties.isTrace();
		}
		
		if (enabled) {
			try {
				traceService.invokeBefore(invocation, serviceConfig);
			} catch (Throwable e) {
				log.error("traceStore invokeBefore failure, error: {}", e.getMessage());
			}
		}
		
		Object result = null;
		Throwable throwable = null;
		
		try {
			result = invocation.proceed();
		} catch (Throwable e) {
			throwable = e;
			throw e;
		} finally {
			if (enabled) {
				try {
					traceService.invokeAfter(invocation, serviceConfig, result, throwable);
				} catch (Throwable e) {
					log.error("traceStore invokeAfter failure, error: {}", e.getMessage());
				}
			}
		}
		return result;
	}
	
	@Override
	public int getOrder() {
		return InterceptorType.TRACE.getOrder();
	}
}
