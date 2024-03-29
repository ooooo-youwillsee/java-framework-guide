package com.ooooo;

import com.ooooo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/12 18:31
 */
@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
public class DubboConsumerApplication {

	@DubboReference
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(DubboConsumerApplication.class, args);
	}

	@Bean
	public ApplicationRunner testApplicationRunner() {
		return __ -> {
			String username = userService.getUsernameById(1L);
			log.info("username: {}", username);
		};
	}
}
