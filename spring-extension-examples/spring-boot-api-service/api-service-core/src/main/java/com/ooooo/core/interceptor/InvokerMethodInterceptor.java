package com.ooooo.core.interceptor;


import com.ooooo.core.Invoker;
import com.ooooo.core.Result;
import com.ooooo.core.beans.InvokerAdapterBeanDefinitionProcessor;
import com.ooooo.core.constants.InterceptorType;
import com.ooooo.core.proxy.APIServiceConfig;
import lombok.AllArgsConstructor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.Ordered;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see InvokerAdapterBeanDefinitionProcessor
 * @since 2021/3/19 21:10
 */
@AllArgsConstructor
public final class InvokerMethodInterceptor extends APIServiceConfigMethodInterceptor implements Ordered {
	
	private final BeanFactory beanFactory;
	private final Class<? extends Invoker> invokerClazz;
	private volatile Invoker invoker;
	
	public InvokerMethodInterceptor(BeanFactory beanFactory, Class<? extends Invoker> invokerClazz) {
		this.beanFactory = beanFactory;
		this.invokerClazz = invokerClazz;
	}
	
	@Override
	public Object doInvoke(MethodInvocation invocation, APIServiceConfig serviceConfig) throws Throwable {
		Result result = getInvoker().invoke(invocation, serviceConfig);
		Class<?> returnType = invocation.getMethod().getReturnType();
		if (returnType.isAssignableFrom(result.getClass())) {
			return result;
		}
		if (result.getException() != null) {
			throw result.getException();
		}
		return result.getValue();
	}
	
	@Override
	public int getOrder() {
		return InterceptorType.INVOKER.getOrder();
	}
	
	private Invoker getInvoker() {
		if (invoker == null) {
			invoker = beanFactory.getBean(invokerClazz);
		}
		return invoker;
	}
}
