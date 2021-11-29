package com.ooooo.core.beans;

import com.ooooo.core.Invoker;
import com.ooooo.core.constants.BeanDefinitionProcessorType;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.interceptor.InvokerMethodInterceptor;
import com.ooooo.core.proxy.APIServiceConfig;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;

/**
 * 指定invokeClass 和对应的serviceType
 *
 * @author leizhijie
 * @since 2021/3/20 09:04
 */
@AllArgsConstructor
public class InvokerAdapterBeanDefinitionProcessor implements BeanDefinitionProcessor, Ordered, ApplicationContextAware {
	
	private final Class<? extends Invoker> invokerClazz;
	private final ServiceType serviceType;
	@Setter
	private ApplicationContext applicationContext;
	
	public InvokerAdapterBeanDefinitionProcessor(Class<? extends Invoker> invokerClazz, ServiceType serviceType) {
		this.invokerClazz = invokerClazz;
		this.serviceType = serviceType;
	}
	
	
	@Override
	public void handle(BeanDefinition holder, APIServiceConfig config) {
		if (serviceType != null && serviceType.equals(config.getServiceType())) {
			config.getTmpAdvices().add(new InvokerMethodInterceptor(applicationContext, invokerClazz));
		}
	}
	
	@Override
	public int getOrder() {
		return BeanDefinitionProcessorType.INVOKER_ADAPTER.getOrder();
	}
}
