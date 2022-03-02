package com.ooooo.annotation;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.MethodIntrospector.MetadataLookup;
import org.springframework.core.Ordered;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 16:01
 */
@Setter
public class SimpleListenerBeanPostProcessor implements BeanPostProcessor, Ordered, SimpleMulticasterAware {

    private SimpleMulticaster simpleMulticaster;
	
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetType = bean.getClass();
		
		// handle @SimpleListener annotation
		Map<Method, SimpleListener> annotatedMethods = MethodIntrospector.selectMethods(targetType, (MetadataLookup<SimpleListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method, SimpleListener.class));
		
		for (Entry<Method, SimpleListener> entry : annotatedMethods.entrySet()) {
			Method method = entry.getKey();
			if (method.getParameterCount() > 1) {
				throw new IllegalArgumentException("'@SimpleListener' parameter count can't greater one");
			}
			simpleMulticaster.addListener(new SimpleListenerAdapter(method, bean, ResolvableType.forMethodParameter(method, 0)));
		}
		
		// handle ISimpleListener interface
		if (bean instanceof ISimpleListener) {
			simpleMulticaster.addListener((ISimpleListener) bean);
		}
		
		return bean;
	}
	
	@Override
	public int getOrder() {
		return SimpleMulticasterAwareBeanPostProcessor.ORDER + 1;
	}
}