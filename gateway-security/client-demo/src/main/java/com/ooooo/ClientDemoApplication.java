package com.ooooo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;

/**
 * @author leizhijie
 * @since 2021/3/12 18:31
 */
@SpringBootApplication
public class ClientDemoApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ClientDemoApplication.class, args);
	}
	
}
