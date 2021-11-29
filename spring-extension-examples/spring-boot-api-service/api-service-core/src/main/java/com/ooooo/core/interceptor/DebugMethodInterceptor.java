package com.ooooo.core.interceptor;


import com.alibaba.fastjson.JSON;
import com.ooooo.core.annotation.APIMapping;
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

/**
 * @author leizhijie
 * @see AdviceBeanDefinitionProcessor
 * @since 2021/3/19 21:10
 */
@Slf4j
@AllArgsConstructor
public final class DebugMethodInterceptor implements MethodInterceptor, Ordered {
	
	public static final String DEBUG_METHOD_INTERCEPTOR_BEAN_NAME = "debugMethodInterceptor";
	
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		AbstractRequestEntity<?> requestEntity = (AbstractRequestEntity<?>) APIServiceContext.getAttribute(invocation, REQUEST_ENTITY_KEY);
		
		prelog(requestEntity, invocation);
		Object result = null;
		try {
			result = invocation.proceed();
		} finally {
			postlog(requestEntity, invocation, result);
		}
		
		return result;
	}
	
	private void prelog(AbstractRequestEntity<?> requestEntity, MethodInvocation invocation) {
		Object[] arguments = invocation.getArguments();
		APIMapping mapping = invocation.getMethod().getAnnotation(APIMapping.class);
		if (mapping == null) return;
		log.info("templateId: {}, requestId: {}, invoke [{}]->{}, args: {} ", requestEntity.getTemplateId(), requestEntity.getRequestId(), mapping.note(), mapping.value(), JSON.toJSONString(arguments));
	}
	
	private void postlog(AbstractRequestEntity<?> requestEntity, MethodInvocation invocation, Object result) {
		APIMapping mapping = invocation.getMethod().getAnnotation(APIMapping.class);
		if (mapping == null) return;
		log.info("templateId: {}, requestId: {},invoke [{}]<-{}, result: {} ", requestEntity.getTemplateId(), requestEntity.getRequestId(), mapping.note(), mapping.value(), (result instanceof byte[]) ? "返回的是字节数组" : JSON.toJSONString(result));
	}
	
	@Override
	public int getOrder() {
		return InterceptorType.DEBUG.getOrder();
	}
}
