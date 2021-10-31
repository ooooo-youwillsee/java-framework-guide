package com.ooooo;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Reference;
import org.mockito.Mockito;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Field;
import java.util.List;

@Component
class ReferenceBeanProcessor implements BeanPostProcessor, BeanFactoryPostProcessor {

	private ConfigurableListableBeanFactory beanFactory;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> userClass = ClassUtils.getUserClass(bean);
		List<Field> annotatedFields = FieldUtils.getFieldsListWithAnnotation(userClass, Reference.class);
		annotatedFields.addAll(FieldUtils.getFieldsListWithAnnotation(userClass, com.alibaba.dubbo.config.annotation.Reference.class));
		annotatedFields.addAll(FieldUtils.getFieldsListWithAnnotation(userClass, DubboReference.class));
		for (Field field : annotatedFields) {
			try {
				Class<?> type = field.getType();
				String mockBeanName = BeanFactoryUtils.transformedBeanName(type.getSimpleName());
				if (!beanFactory.containsBean(mockBeanName)) {
					beanFactory.registerSingleton(mockBeanName, Mockito.mock(type));
				}

				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.set(bean, beanFactory.getBean(type));
				field.setAccessible(accessible);
			} catch (IllegalAccessException e) {
			}
		}
		return bean;
	}
}