package com.ooooo.config;

import com.alibaba.cloud.nacos.ConditionalOnNacosDiscoveryEnabled;
import com.alibaba.cloud.nacos.registry.NacosAutoServiceRegistration;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.serviceregistry.AbstractAutoServiceRegistration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * <p>运行在 tomcat 下，不会发布 WebServerInitializedEvent</p>
 * <p>
 * <p>
 * see WebServerStartStopLifecycle
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see WebServerInitializedEvent
 * @see AbstractAutoServiceRegistration#onApplicationEvent(WebServerInitializedEvent) //
 * @see NacosAutoServiceRegistration#register()
 */
@Configuration
@ConditionalOnDiscoveryEnabled
@ConditionalOnNacosDiscoveryEnabled
@ConditionalOnWebApplication
@ConditionalOnBean(NacosAutoServiceRegistration.class)
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