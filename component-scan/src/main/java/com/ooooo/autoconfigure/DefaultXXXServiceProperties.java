package com.ooooo.autoconfigure;

import com.ooooo.properties.ServiceProperties;

/**
 * @author leizhijie
 * @since 2021/2/22 15:16
 */
public class DefaultXXXServiceProperties implements ServiceProperties {
	
	@Override
	public String getIp() {
		return "127.0.0.1";
	}
	
	@Override
	public String getPort() {
		return "8081";
	}
	
	@Override
	public String getPrefix() {
		return "/";
	}
}
