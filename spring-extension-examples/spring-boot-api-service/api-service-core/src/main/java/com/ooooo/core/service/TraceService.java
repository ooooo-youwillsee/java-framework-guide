package com.ooooo.core.service;

import com.ooooo.core.proxy.APIServiceConfig;
import org.aopalliance.intercept.MethodInvocation;

/**
 * trace request
 *
 * @author leizhijie
 * @since 2021/8/26 09:33
 */
public interface TraceService {
	
	void invokeBefore(MethodInvocation invocation, APIServiceConfig serviceConfig) throws Throwable;
	
	void invokeAfter(MethodInvocation invocation, APIServiceConfig serviceConfig, Object result, Throwable throwable) throws Throwable;
}
