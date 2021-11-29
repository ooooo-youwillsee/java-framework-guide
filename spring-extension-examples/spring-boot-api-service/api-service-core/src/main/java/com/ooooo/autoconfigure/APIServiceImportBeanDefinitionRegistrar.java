package com.ooooo.autoconfigure;

import com.ooooo.core.annotation.APIServiceComponentScan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

/**
 * @author leizhijie
 * @since 2021/3/12 16:18
 */
public class APIServiceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
	
	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry, BeanNameGenerator importBeanNameGenerator) {
		
		MultiValueMap<String, Object> allAnnotationAttributes = importingClassMetadata.getAllAnnotationAttributes(APIServiceComponentScan.class.getName());
		if (allAnnotationAttributes != null) {
			List<Object> values = allAnnotationAttributes.get("value");
			List<Object> basePackages = allAnnotationAttributes.get("basePackages");
			for (int i = 0; i < values.size(); i++) {
				Map<String, Object> map = new HashMap<>();
				map.put("value", values.get(i));
				map.put("basePackages", basePackages.get(i));
				AnnotationAttributes attr = AnnotationAttributes.fromMap(map);
				registerClassPathServiceScanner(importingClassMetadata, attr, registry);
			}
		}
	}
	
	private void registerClassPathServiceScanner(AnnotationMetadata importingClassMetadata, AnnotationAttributes attr, BeanDefinitionRegistry registry) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ClassPathAPIServiceScannerConfigurer.class);
		
		List<String> basePackages = new ArrayList<>();
		basePackages.addAll(Arrays.stream(attr.getStringArray("value")).filter(StringUtils::hasText).collect(Collectors.toList()));
		basePackages.addAll(Arrays.stream(attr.getStringArray("basePackages")).filter(StringUtils::hasText).collect(Collectors.toList()));
		
		if (basePackages.isEmpty()) {
			basePackages.add(getDefaultBasePackage(importingClassMetadata));
		}
		
		builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(basePackages));
		
		String beanName = importingClassMetadata.getClassName() + "#" + ClassPathAPIServiceScannerConfigurer.class.getName();
		BeanDefinitionHolder holder = new BeanDefinitionHolder(builder.getBeanDefinition(), generateBeanName(beanName, registry));
		BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
	}
	
	
	private static String getDefaultBasePackage(AnnotationMetadata importingClassMetadata) {
		return ClassUtils.getPackageName(importingClassMetadata.getClassName());
	}
	
	private static String generateBeanName(String beanName, BeanDefinitionRegistry registry) {
		int i = 0;
		String newBeanName;
		while (true) {
			newBeanName = beanName + i;
			if (!registry.containsBeanDefinition(newBeanName)) {
				break;
			}
			i++;
		}
		return newBeanName;
	}
}
