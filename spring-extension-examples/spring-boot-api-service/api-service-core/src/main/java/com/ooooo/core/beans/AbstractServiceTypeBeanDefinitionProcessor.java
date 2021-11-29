package com.ooooo.core.beans;

import com.ooooo.core.annotation.IAPIService;
import com.ooooo.core.constants.BeanDefinitionProcessorType;
import com.ooooo.core.constants.ServiceType;
import com.ooooo.core.proxy.APIServiceConfig;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.core.Ordered;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.ClassUtils;

/**
 * recognize serviceType of beanDefinition
 *
 * @author leizhijie
 * @since 2021/3/19 20:11
 */
public abstract class AbstractServiceTypeBeanDefinitionProcessor implements BeanDefinitionProcessor, Ordered {
	
	@Getter
	private final AntPathMatcher antPathMatcher;
	
	public AbstractServiceTypeBeanDefinitionProcessor() {
		antPathMatcher = new AntPathMatcher(".");
	}
	
	@Override
	public void handle(BeanDefinition beanDefinition, APIServiceConfig config) {
		if (support(beanDefinition)) {
			config.setServiceType(getServiceType());
		}
	}
	
	protected boolean support(BeanDefinition beanDefinition) {
		String beanClassName = beanDefinition.getBeanClassName();
		String beanPackageName = ClassUtils.getPackageName(beanClassName);
		
		// 包名识别
		for (String name : getPackageNames()) {
			if (name != null && beanPackageName.startsWith(name)) {
				return true;
			}
		}
		
		// 注解识别
		if (beanDefinition instanceof AnnotatedBeanDefinition) {
			AnnotatedBeanDefinition annotatedBeanDefinition = (AnnotatedBeanDefinition) beanDefinition;
			AnnotationMetadata metadata = annotatedBeanDefinition.getMetadata();
			if (getAnnotation() != null && metadata.hasAnnotation(getAnnotation().getName())) {
				return true;
			}
		}
		
		// 接口识别
		if (beanClassName != null) {
			try {
				Class<?> clazz = ClassUtils.forName(beanClassName, Thread.currentThread().getContextClassLoader());
				return getInterface() != null && getInterface().isAssignableFrom(clazz);
			} catch (ClassNotFoundException ignored) {
			}
		}
		
		return false;
	}
	
	// 表明哪个注解可以识别为ServiceType，建议接口与注解一一对应
	protected Class<? extends Annotation> getAnnotation() {
		return null;
	}
	
	// 表明哪个接口可以识别为ServiceType, 建议接口与注解一一对应
	protected Class<? extends IAPIService> getInterface() {
		return null;
	}
	
	// 表明哪个包可以识别为ServiceType
	protected Set<String> getPackageNames() {
		return new HashSet<>();
	}
	
	// 识别的服务类型
	protected abstract ServiceType getServiceType();
	
	
	public int getOrder() {
		return BeanDefinitionProcessorType.SERVIC_TYPE.getOrder();
	}
	
}
