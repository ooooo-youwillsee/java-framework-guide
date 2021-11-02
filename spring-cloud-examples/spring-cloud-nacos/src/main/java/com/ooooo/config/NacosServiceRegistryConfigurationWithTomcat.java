package com.ooooo.config;

import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p>运行在 tomcat 下，不会发布 WebServerInitializedEvent</p>
 * <p>
 * <p>
 * see WebServerStartStopLifecycle
 *
 * @see WebServerInitializedEvent
 * @see AbstractAutoServiceRegistration#onApplicationEvent(WebServerInitializedEvent) //
 * @see NacosAutoServiceRegistration#register()
 */
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass(NacosAutoServiceRegistration.class)
public class NacosServiceRegistryConfigurationWithTomcat {
	
	@Autowired(required = false)
	private NacosAutoServiceRegistration registration;
	
	@Autowired
	private Environment env;
	
	@PostConstruct
	public void postConstruct() {
		if (registration != null) {
			int serverPort = Integer.parseInt(
					env.getProperty("server.port", env.getProperty("port", "8080")));
			registration.setPort(serverPort);
			registration.start();
		}
		
	}
	
	
}