package com.ooooo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author leizhijie
 * @since 2021/4/12 20:52
 */
@SpringBootApplication
@EnableRedisRepositories
public class RedisApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}
}
