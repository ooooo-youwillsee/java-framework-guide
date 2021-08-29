package com.ooooo.annotation;

import com.ooooo.autoconfigure.APIServiceFactory;
import com.ooooo.config.APIServiceBeanDefinitionRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author leizhijie
 * @since 2021/2/22 15:05
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(APIServiceBeanDefinitionRegistrar.class)
public @interface APIServiceComponentScan {
	
	String[] basePackage() default {};
	
	Class<?> serviceFactory() default APIServiceFactory.class;
}
