package com.ooooo.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author leizhijie
 * @since 2021/4/12 21:17
 */
@Configuration
public class RedisTemplateConfig {
	
	
	/**
	 * 覆盖 {@see RedisAutoConfiguration} 默认提供的
	 * @see RedisAutoConfiguration
	 */
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<Object, Object> template = new RedisTemplate<>();
		// stringRedisTemplate 也是配置以下四个, key 直接用 StringRedisSerializer
		template.setKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new FastJsonRedisSerializer<>(Object.class));
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(new FastJsonRedisSerializer<>(Object.class));
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}
	
}
