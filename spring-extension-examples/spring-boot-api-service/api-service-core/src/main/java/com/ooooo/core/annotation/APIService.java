package com.ooooo.core.annotation;

import com.ooooo.core.request.ITemplate;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leizhijie
 * @since 2021/3/8 17:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Documented
@Inherited
public @interface APIService {
	
	/**
	 * ITemplate接口的Id
	 *
	 * @return
	 * @see ITemplate
	 */
	String templateId() default "";
}
