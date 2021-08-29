package com.ooooo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.HttpMethod;

/**
 * @author leizhijie
 * @since 2021/2/24 15:21
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface XRequestMapping {
	
	// mapping's path
	String value() default "";
	
	// mapping support method
	HttpMethod[] method() default {};
}
