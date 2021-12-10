package com.ooooo.core.annotation;

import com.ooooo.core.request.ITemplate;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leizhijie
 * @since 2021/3/8 17:01
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface APIMapping {
	
	String value() default "";
	
	/**
	 * ITemplate接口的Id
	 *
	 * @return
	 * @see ITemplate
	 */
	String templateId() default "";
	
	String note() default "";
	
	/**
	 * auto 不做任何判断，由对应的invoker来自己处理
	 *
	 * @return
	 */
	APISeriviceContentTypeEnums contentType() default APISeriviceContentTypeEnums.AUTO;
}
