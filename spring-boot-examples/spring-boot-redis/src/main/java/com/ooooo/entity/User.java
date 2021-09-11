package com.ooooo.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

/**
 * @author leizhijie
 * @see <a href=https://docs.spring.io/spring-data/redis/docs/2.4.7/reference/html/#redis.repositories>redis.repositories</a>
 * @since 2021/4/12 21:49
 */
@Data
@RedisHash("user")
public class User {

	@Id
	private Long id;

	private String username;

	private Integer age;

	private String address;
}
