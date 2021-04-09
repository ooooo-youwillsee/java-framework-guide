package com.ooooo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.HttpMethod;

/**
 * @author leizhijie
 * @since 2021/2/22 15:18
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface APIMapping {
	
	String value();
	
	HttpMethod method() default HttpMethod.GET;
	
}
