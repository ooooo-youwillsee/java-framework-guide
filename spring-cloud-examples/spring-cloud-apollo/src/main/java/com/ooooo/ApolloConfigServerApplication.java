package com.ooooo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/12 18:31
 */
@SpringBootApplication
@Slf4j
public class ApolloConfigServerApplication {

	@Value("${aaa:111}")
	private String aaa;

	public static void main(String[] args) {
		SpringApplication.run(ApolloConfigServerApplication.class, args);
	}

	@Bean
	public ApplicationRunner testApplicationRunner() {
		return args -> {
			log.info("receive data: {}", aaa);
		};
	}


}
