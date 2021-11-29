package com.ooooo.autoconfigure;

import com.ooooo.core.annotation.APIService;
import com.ooooo.core.annotation.IAPIService;
import com.ooooo.core.beans.BeanDefinitionProcessor;
import com.ooooo.core.proxy.APIServiceConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

/**
 * @author leizhijie
 * @since 2021/1/13 09:19
 */
@Slf4j
public class ClassPathAPIServiceScanner extends ClassPathBeanDefinitionScanner {
	
	@Setter
	private ApplicationContext applicationContext;
	
	public ClassPathAPIServiceScanner(BeanDefinitionRegistry registry) {
		super(registry);
	}
	
	@Override
	protected void registerDefaultFilters() {
		addIncludeFilter(new AnnotationTypeFilter(APIService.class));
	}
	
	@Override
	protected Set<BeanDefinitionHolder> doScan(String... basePackages) {
		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
		if (beanDefinitions.isEmpty()) {
			log.warn("No APIService was found in '{}' package. Please check your configuration.", Arrays.toString(basePackages));
		} else {
			processBeanDefinitions(beanDefinitions);
		}
		
		return beanDefinitions;
	}
	
	private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
		for (BeanDefinitionHolder holder : beanDefinitions) {
			AbstractBeanDefinition beanDefinition = (AbstractBeanDefinition) holder.getBeanDefinition();
			APIServiceConfig config = new APIServiceConfig();
			config.setApplicationContext(applicationContext);
			finishBeanDefinition(beanDefinition, config);
		}
	}
	
	protected void processBeanDefiniton(AbstractBeanDefinition beanDefinition, APIServiceConfig config) {
		List<BeanDefinitionProcessor> beanDefinitionProcessors = new ArrayList<>(applicationContext.getBeansOfType(BeanDefinitionProcessor.class).values());
		AnnotationAwareOrderComparator.sort(beanDefinitionProcessors);
		for (BeanDefinitionProcessor processor : beanDefinitionProcessors) {
			processor.handle(beanDefinition, config);
		}
	}
	
	protected void finishBeanDefinition(AbstractBeanDefinition beanDefinition, APIServiceConfig config) {
		String beanClassName = beanDefinition.getBeanClassName();
		Class<?> beanClass = ClassUtils.resolveClassName(beanClassName, Thread.currentThread().getContextClassLoader());
		beanDefinition.setBeanClass(beanClass);
		beanDefinition.setBeanClassName(beanClassName);
		
		beanDefinition.setInstanceSupplier(() -> {
			ClassPathAPIServiceScanner.this.processBeanDefiniton(beanDefinition, config);
			
			ProxyFactoryBean proxyBean = new ProxyFactoryBean();
			proxyBean.addInterface(IAPIService.class);
			proxyBean.addInterface(beanClass);
			proxyBean.setExposeProxy(true);
			proxyBean.setTarget(config);
			
			// extension
			proxyBean.addAdvice(0, (MethodInterceptor) invocation -> {
				String name = invocation.getMethod().getName();
				if (name.equals("toString")) {
					return beanClassName;
				}
				if (name.equals("getAPIServiceConfig")) {
					return config;
				}
				if (name.equals("getProxyFactoryBean")) {
					return proxyBean;
				}
				return invocation.proceed();
			});
			for (Advice tmpAdvice : config.getTmpAdvices()) {
				proxyBean.addAdvice(tmpAdvice);
			}
			
			return proxyBean;
		});
	}
	
	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		AnnotationMetadata metadata = beanDefinition.getMetadata();
		return beanDefinition instanceof AbstractBeanDefinition && !metadata.isAnnotation();
	}
}
