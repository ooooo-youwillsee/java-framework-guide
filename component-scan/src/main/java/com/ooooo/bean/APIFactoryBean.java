package com.ooooo.bean;

import com.ooooo.factory.ServiceFactory;
import lombok.Setter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author leizhijie
 * @since 2021/2/22 15:30
 * <p>
 * convert interface to FactoryBean
 */
@Setter
public class APIFactoryBean<T> implements FactoryBean<T>, InitializingBean {
	
	public ServiceFactory serviceFactory;
	public Class<T> interfaceClazz;
	
	
	@Override
	public T getObject() throws Exception {
		return serviceFactory.getService(interfaceClazz);
	}
	
	@Override
	public Class<?> getObjectType() {
		return interfaceClazz;
	}
	
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// not lazy
		serviceFactory.getService(interfaceClazz);
	}
}
