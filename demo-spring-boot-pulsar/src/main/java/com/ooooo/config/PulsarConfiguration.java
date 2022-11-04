package com.ooooo.config;

import lombok.SneakyThrows;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ooooo
 * @date 2021/10/10 09:40
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(PulsarProperties.class)
public class PulsarConfiguration {

	@Autowired
	private PulsarProperties pulsarProperties;

	@SneakyThrows
	@Bean(destroyMethod = "close")
	public PulsarClient pulsarClient() {
		return PulsarClient.builder()
						.serviceUrl(pulsarProperties.getServiceUrl())
						.build();
	}

}
