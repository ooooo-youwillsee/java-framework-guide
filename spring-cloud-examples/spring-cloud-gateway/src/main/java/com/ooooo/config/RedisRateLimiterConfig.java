package com.ooooo.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/4/4 22:51
 */
@Configuration
public class RedisRateLimiterConfig {
	
	// 必须重写这个bean
	@Bean
	KeyResolver userKeyResolver() {
		// 每个url 视为一个用户请求
		return exchange -> Mono.just(exchange.getRequest().getURI().toString());
	}
}
