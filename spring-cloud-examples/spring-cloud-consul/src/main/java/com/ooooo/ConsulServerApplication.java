package com.ooooo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author leizhijie
 * @since 2021/3/12 18:31
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsulServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsulServerApplication.class, args);
	}

}
