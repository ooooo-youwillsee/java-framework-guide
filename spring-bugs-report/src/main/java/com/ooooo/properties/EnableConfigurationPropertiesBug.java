package com.ooooo.properties;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * EnableConfigurationProperties 注解只能放在 @Configuration 的类上面
 *
 * @author ooooo
 * @since 2021/07/11 15:43
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(TestHelloProperties.class)
public class EnableConfigurationPropertiesBug {

	@Autowired
	private TestHelloProperties testHelloProperties;

	@Bean
	public ApplicationRunner testApplicationRunner() {
		return (args) -> {
			log.info("{}", testHelloProperties);
		};
	}


}
