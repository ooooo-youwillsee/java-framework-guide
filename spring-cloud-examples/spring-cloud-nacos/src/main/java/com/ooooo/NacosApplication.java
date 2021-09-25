package com.ooooo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author leizhijie
 * @since 1.0
 */
@Slf4j
@SpringBootApplication
public class NacosApplication {

	@Value("${aaa:111}")
	private String aaa;

	public static void main(String[] args) {
		SpringApplication.run(NacosApplication.class, args);
	}

	@Bean
	public ApplicationRunner testApplicationRunner() {
		return args -> {
			log.info("the value of property 'aaa' is {}", aaa);
		};
	}
}
