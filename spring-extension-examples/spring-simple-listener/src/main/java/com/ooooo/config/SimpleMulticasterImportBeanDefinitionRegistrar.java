package com.ooooo.config;

import com.ooooo.annotation.SimpleMulticaster;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/24 09:23
 */
public class SimpleMulticasterImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	
	@Override
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
		try {
			AbstractBeanDefinition beanDefinition = BeanDefinitionReaderUtils.createBeanDefinition(null, SimpleMulticaster.class.getName(), registry.getClass().getClassLoader());
			BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition, registry);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
