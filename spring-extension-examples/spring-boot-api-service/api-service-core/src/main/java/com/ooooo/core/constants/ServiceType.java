package com.ooooo.core.constants;

import com.cairh.cpe.core.autoconfiure.env.CompositePropertySources;
import com.ooooo.core.annotation.OnlyForTest;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

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
	
	public static ServiceType current(CompositePropertySources compositePropertySources) {
		String type = compositePropertySources.getProperty(CONFIG_SERVICE_TYPE);
		if (StringUtils.isBlank(type)) {
			throw new IllegalArgumentException("serviceType isn't config");
		}
		return ServiceType.of(type);
	}
}
