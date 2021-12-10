package com.ooooo.core.constants;

import com.ooooo.core.annotation.OnlyForTest;
import lombok.Getter;

/**
 * @author leizhijie
 * @since 2021/3/19 20:23
 */
public enum ServiceType {
	
	XPE_SERVICE("xpe"),
	T2_SERVICE("t2"),
	T3_SERVICE("t3"),
	KCBP_SERVICE("kcbp"),
	DB_SERVICE("db"),
	PROXY_SERVICE("proxy"),
	HS_OPENAPI_SERVICE("hs_openapi"),
	
	@OnlyForTest
	TEST_SERVICE("test"),
	;
	
	
	@Getter
	private final String type;
	
	ServiceType(String type) {
		this.type = type;
	}
	
	public static ServiceType of(String type) {
		for (ServiceType serviceType : values()) {
			if (serviceType.getType().equals(type)) {
				return serviceType;
			}
		}
		throw new IllegalArgumentException(type + "doesn't exist");
	}
	
	public static final String CONFIG_SERVICE_TYPE = "serviceType";
	
}
