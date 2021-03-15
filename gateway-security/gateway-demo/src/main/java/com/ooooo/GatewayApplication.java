package com.ooooo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.tools.agent.ReactorDebugAgent;

/**
 * @author leizhijie
 * @since 2021/3/12 18:31
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class GatewayApplication {
	
	public static void main(String[] args) {
		ReactorDebugAgent.init();
		SpringApplication.run(GatewayApplication.class, args);
	}
	
	@GetMapping("/gateway")
	public Mono<String> getGateway() {
		return Mono.just("getGateway");
	}
	
}
