package com.ooooo.annotation;

import com.ooooo.autoconfigure.DefaultXXXServiceFactory;
import com.ooooo.config.XXXBeanDefinitionRegistrar;
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
@Import(XXXBeanDefinitionRegistrar.class)
public @interface XXXComponentScan {
	
	String[] basePackage() default {};
	
	Class<?> serviceFactory() default DefaultXXXServiceFactory.class;
}
