package com.ooooo.core.annotation;

import com.ooooo.autoconfigure.APIServiceImportBeanDefinitionRegistrar;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.context.annotation.Import;

/**
 * @author leizhijie
 * @since 2021/3/8 17:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Inherited
@Import(APIServiceImportBeanDefinitionRegistrar.class)
public @interface APIServiceComponentScan {
	
	String value() default "";
	
	String[] basePackages() default {};
	
}
