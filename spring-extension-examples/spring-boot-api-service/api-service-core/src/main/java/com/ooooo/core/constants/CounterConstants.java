package com.ooooo.core.constants;

import com.ooooo.core.annotation.APIMapping;
import com.ooooo.core.request.IRequestEntityProcessor;
import com.ooooo.core.request.ITemplate;

/**
 * @author leizhijie
 * @since 1.0.0
 */
public interface CounterConstants {
	
	String PREFIX_CPE_SERVICE = "cpe.service";
	
	/**
	 * template_id
	 *
	 * @see ITemplate
	 */
	String TEMPLATE_ID_KEY = "_TEMPLATE_ID";
	
	/**
	 * template_id
	 *
	 * @see ITemplate
	 */
	String TEMPLATE_ID_DEFAULT_VALUE = "default";
	
	
	/**
	 * @see IRequestEntityProcessor
	 */
	String REQUEST_ENTITY_KEY = "_REQUEST_ENTITY_KEY";
	
	/**
	 * @see IRequestEntityProcessor
	 */
	String APISERVICE_CONFIG_KEY = "_APISERVICE_CONFIG_KEY";
	
	/**
	 * APIMapping 注解的value为 '*' ，表明为泛化调用
	 *
	 * @see APIMapping
	 */
	String GENERIC_SERVICE_URL_VALUE = "*";
	
	/**
	 * 泛化调用时，传入的url的key
	 *
	 * @see APIMapping
	 */
	String GENERIC_SERVICE_URL_KEY = "_GENERIC_SERVICE_URL";
	
	
	String SECURITY_ALIAS = "security.alias";
	
	String FUNCTION_NAME = "_FUNCTION_NAME";
	
	String REQUEST_ID_KEY = "_REQUEST_ID";
	
	/**
	 * 启动
	 */
	String ENABLED = "1";
	
	/**
	 * 禁用
	 */
	String DISENABLED = "0";
}
