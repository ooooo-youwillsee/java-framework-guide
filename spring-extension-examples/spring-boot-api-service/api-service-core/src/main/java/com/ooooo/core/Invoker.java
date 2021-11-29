package com.ooooo.core;

import com.ooooo.core.exception.APIException;
import com.ooooo.core.proxy.APIServiceConfig;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author leizhijie
 * @since 2021/3/18 16:33
 */
public interface Invoker {
	
	Result invoke(MethodInvocation invocation, APIServiceConfig serviceConfig) throws APIException;
	
}
