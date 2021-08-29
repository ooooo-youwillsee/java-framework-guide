package com.ooooo.config;

import com.ooooo.annotation.APIService;
import com.ooooo.bean.APIFactoryBean;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;

/**
 * @author leizhijie
 * @since 2021/2/22 15:56
 * <p>
 * method for scan
 */
public class APIServiceComponentScanner extends ClassPathBeanDefinitionScanner {
	
	private final BeanNameGenerator beanNameGenerator = AnnotationBeanNameGenerator.INSTANCE;
	public Class<?> serviceFactory;
	
	public APIServiceComponentScanner(BeanDefinitionRegistry registry, Class<?> serviceFactory) {
		super(registry, true);
		setIncludeAnnotationConfig(false);
		this.serviceFactory = serviceFactory;
	}
	
	@Override
	protected void registerDefaultFilters() {
		addIncludeFilter(new AnnotationTypeFilter(APIService.class));
	}
	
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Assert.notEmpty(basePackages, "At least one base package must be specified");
		Set<BeanDefinitionHolder> beanDefinitions = new LinkedHashSet<>();
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			for (BeanDefinition candidate : candidates) {
				String beanName = this.beanNameGenerator.generateBeanName(candidate, getRegistry());
				if (candidate instanceof AbstractBeanDefinition) {
					postProcessBeanDefinition((AbstractBeanDefinition) candidate, beanName);
				}
				if (checkCandidate(beanName, candidate)) {
					// apply to factoryBean
					String interfaceClassName = candidate.getBeanClassName();
					candidate.getPropertyValues().add("interfaceClazz", interfaceClassName);
					candidate.getPropertyValues().add("serviceFactory", new RuntimeBeanReference(serviceFactory));
					candidate.setBeanClassName(APIFactoryBean.class.getName());
					
					BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(candidate, beanName);
					beanDefinitions.add(definitionHolder);
					registerBeanDefinition(definitionHolder, getRegistry());
				}
			}
		}
		return beanDefinitions;
	}
	
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		AnnotationMetadata metadata = beanDefinition.getMetadata();
		Set<String> annotationTypes = metadata.getAnnotationTypes();
		return annotationTypes.contains(APIService.class.getName());
	}
	
	
}
