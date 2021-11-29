package com.ooooo.core.annotation;

import com.ooooo.core.proxy.APIServiceConfig;
import org.springframework.aop.framework.ProxyFactoryBean;

/**
 * @author leizhijie
 * @since 2021/3/19 20:16
 */
public interface IAPIService {
	
	APIServiceConfig getAPIServiceConfig();
	
	ProxyFactoryBean getProxyFactoryBean();
}
