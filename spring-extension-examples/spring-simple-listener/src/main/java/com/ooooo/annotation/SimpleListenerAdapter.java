package com.ooooo.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.springframework.core.ResolvableType;
import org.springframework.util.ReflectionUtils;

/**
 * @author leizhijie
 * @since 2021/2/23 17:20
 */
public class SimpleListenerAdapter implements ISimpleListener<Object> {
	
	private final Method method;
	private final Object bean;
	private final ResolvableType parameterResolvableType;
	
	public SimpleListenerAdapter(Method method, Object beanName, ResolvableType paramType) {
		this.method = method;
		this.bean = beanName;
		this.parameterResolvableType = paramType;
	}
	
	@Override
	public void invoke(Object msg) {
		ReflectionUtils.makeAccessible(method);
		try {
			method.invoke(bean, msg);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	
	public ResolvableType getParameterResolvableType() {
		return parameterResolvableType;
	}
}
