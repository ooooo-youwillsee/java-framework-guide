package com.ooooo.annotation;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

/**
 * @author leizhijie
 * @since 2021/2/24 09:12
 */
@Setter
public class SimpleMulticasterAwareBeanPostProcessor implements BeanPostProcessor, PriorityOrdered, BeanFactoryAware {
	
	public static final int ORDER = Ordered.HIGHEST_PRECEDENCE;
	
	private BeanFactory beanFactory;
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (bean instanceof SimpleMulticasterAware) {
			SimpleMulticasterAware simpleMulticaster = (SimpleMulticasterAware) bean;
			simpleMulticaster.setSimpleMulticaster(beanFactory.getBean(SimpleMulticaster.class));
		}
		return bean;
	}
	
	@Override
	public int getOrder() {
		return ORDER;
	}
}
