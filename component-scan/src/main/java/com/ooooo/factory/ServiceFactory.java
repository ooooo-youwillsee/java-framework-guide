package com.ooooo.factory;

/**
 * @author leizhijie
 * @since 2021/2/22 15:07
 */
public interface ServiceFactory {
	
	<T> T getService(Class<T> interfaceClazz);
}
