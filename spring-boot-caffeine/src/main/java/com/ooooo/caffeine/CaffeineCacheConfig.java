package com.ooooo.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ooooo
 * @date 2021/07/21 19:56
 */
@Configuration
public class CaffeineCacheConfig {

	@Bean
	public Cache<String, String> syncCaffeineConfig() {
		return Caffeine.newBuilder()
						.expireAfterWrite(5, TimeUnit.MINUTES)
						.expireAfterAccess(5, TimeUnit.SECONDS)
						.maximumSize(10_000)
						.build();
	}

}
