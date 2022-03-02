package com.ooooo.autoconfigure;

import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/12 16:31
 */
public class ClassPathAPIServiceScannerConfigurer implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {
	
	@Setter
	private String[] basePackage;
	
	@Setter
	private ApplicationContext applicationContext;
	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ClassPathAPIServiceScanner scanner = new ClassPathAPIServiceScanner(registry);
		scanner.setBeanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator());
		scanner.setApplicationContext(applicationContext);
		scanner.doScan(basePackage);
	}
	
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// no implements
	}
}
