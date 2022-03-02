package com.ooooo.core.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/5/31 09:16
 */
@Getter
@AllArgsConstructor
public enum APISeriviceContentTypeEnums {
	
	AUTO("auto"), // 由相应的invoker自己识别
	APPLICATION_JSON_VALUE("application/json"),
	APPLICATION_FORM_URLENCODED_VALUE("application/x-www-form-urlencoded"),
	;
	
	private final String value;
}
