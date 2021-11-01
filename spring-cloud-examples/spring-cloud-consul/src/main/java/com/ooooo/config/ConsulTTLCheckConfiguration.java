package com.ooooo.config;

import com.ecwid.consul.v1.agent.model.NewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.consul.discovery.ConditionalOnConsulDiscoveryEnabled;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.HeartbeatProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationListener;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistrationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 解决没有 web 端口情况下，健康检查失效
 *
 * @author leizhijie
 * @see ConsulAutoRegistration#createCheck(Integer, HeartbeatProperties, ConsulDiscoveryProperties)
 * @see ConsulAutoServiceRegistration#getRegistration()
 * @see ConsulAutoServiceRegistration#setPortIfNeeded(int)
 * @see ConsulAutoServiceRegistrationListener
 * @since 1.0.0
 */

@Slf4j
@Configuration
@ConditionalOnNotWebApplication
@ConditionalOnDiscoveryEnabled
@ConditionalOnConsulDiscoveryEnabled
@EnableConfigurationProperties({HeartbeatProperties.class, ConsulDiscoveryProperties.class})
public class ConsulTTLCheckConfiguration {
	
	@Autowired
	private HeartbeatProperties ttlConfig;
	
	@Autowired
	private ConsulDiscoveryProperties properties;
	
	
	@Bean
	public ConsulRegistrationCustomizer ttlCheck() {
		return registration -> {
			NewService service = registration.getService();
			if (ttlConfig.isEnabled()) {
				log.info("非 WEB 环境开启，consul 开启 TTL 健康检查");
				
				if (service.getCheck() == null) {
					NewService.Check check = ConsulAutoRegistration.createCheck(1000, ttlConfig, properties);
					service.setCheck(check);
				}
			}
		};
	}
	
}
