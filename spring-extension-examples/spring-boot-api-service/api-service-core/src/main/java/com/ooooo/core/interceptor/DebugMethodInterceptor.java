package com.ooooo.core.interceptor;


import com.ooooo.core.beans.AdviceBeanDefinitionProcessor;
import com.ooooo.core.constants.InterceptorType;
import com.ooooo.core.context.APIServiceContext;
import com.ooooo.core.request.AbstractRequestEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.Ordered;

import static com.ooooo.core.constants.CounterConstants.REQUEST_ENTITY_KEY;
import static com.ooooo.core.util.ParamUtil.toJSONString;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see AdviceBeanDefinitionProcessor
 * @since 2021/3/19 21:10
 */
@Slf4j
@AllArgsConstructor
public final class DebugMethodInterceptor implements MethodInterceptor, Ordered {
	
	public static final String DEBUG_METHOD_INTERCEPTOR_BEAN_NAME = "debugMethodInterceptor";
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		AbstractRequestEntity<?> request = (AbstractRequestEntity<?>) APIServiceContext.getAttribute(invocation, REQUEST_ENTITY_KEY);
		
		prelog(request);
		Object result = null;
		try {
			result = invocation.proceed();
		} finally {
			postlog(request, result);
		}
		
		return result;
	}
	
	private void prelog(AbstractRequestEntity<?> request) {
		log.info("templateId: {}, requestId: {}, invoke [{}]->{}, args: {} ", request.getTemplateId(), request.getRequestId(), request.getApiMappingNote(),
		         request.getUrl(), toJSONString(request.getParams()));
	}
	
	private void postlog(AbstractRequestEntity<?> request, Object result) {
		log.info("templateId: {}, requestId: {}, invoke [{}]<-{}, result: {} ", request.getTemplateId(), request.getRequestId(), request.getApiMappingNote(),
		         request.getUrl(), (result instanceof byte[]) ? "返回的是字节数组" : toJSONString(result));
	}
	
	@Override
	public int getOrder() {
		return InterceptorType.DEBUG.getOrder();
	}
}
