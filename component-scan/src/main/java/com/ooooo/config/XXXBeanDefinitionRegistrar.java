package com.ooooo.config;

import com.ooooo.annotation.XXXComponentScan;
import com.ooooo.autoconfigure.DefaultXXXServiceFactory;
import java.util.Map;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

/**
 * @author leizhijie
 * @since 2021/2/22 16:39
 */
public class XXXBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
		Map<String, Object> attributes = importingClassMetadata.getAnnotationAttributes(XXXComponentScan.class.getName());
		String[] basePackage = (String[]) attributes.get("basePackage");
		Class<?> serviceFactory = (Class<?>) attributes.get("serviceFactory");
		if (basePackage == null || basePackage.length == 0) {
			basePackage = new String[]{ClassUtils.getPackageName(importingClassMetadata.getClassName())};
		}
		
		XXXComponentScanner scanner = new XXXComponentScanner(registry, serviceFactory);
		scanner.scan(basePackage);
	}
	
}
