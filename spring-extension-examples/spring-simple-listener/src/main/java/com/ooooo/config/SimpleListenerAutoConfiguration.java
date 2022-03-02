package com.ooooo.config;

import com.ooooo.annotation.SimpleListenerBeanPostProcessor;
import com.ooooo.annotation.SimpleMulticasterAwareBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/23 16:01
 */
@Configuration
@Import(SimpleMulticasterImportBeanDefinitionRegistrar.class)
public class SimpleListenerAutoConfiguration {
	
	@Bean
	public SimpleListenerBeanPostProcessor simpleListenerBeanPostProcessor() {
		return new SimpleListenerBeanPostProcessor();
	}
	
	@Bean
	public SimpleMulticasterAwareBeanPostProcessor simpleMulticasterAwareBeanProcessor() {
		return new SimpleMulticasterAwareBeanPostProcessor();
	}
}
