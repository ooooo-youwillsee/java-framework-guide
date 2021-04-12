package com.ooooo.basic;

import com.ooooo.RedisApplicationTests;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author leizhijie
 * @since 2021/4/12 20:54
 * <p>
 * redis 基本操作测试类
 */
public class BasicOperationTests extends RedisApplicationTests {
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	
	private static final String KEY = "test-key";
	
	private Map<String, Object> info;
	
	@BeforeEach
	public void before() {
		info = new HashMap<>();
		info.put("username", "tom");
		info.put("age", 18);
		info.put("password", "111111");
	}
	
	@Test
	public void setAndGetByStringRedisTemplate() {
		BoundValueOperations<String, String> ops = stringRedisTemplate.boundValueOps(KEY);
		ops.set("tom");
		Assertions.assertEquals(ops.get(), "tom");
		stringRedisTemplate.delete(KEY);
		Assertions.assertNull(ops.get());
	}
	
	
	@Test
	public void setAndGetByRedisTemplate() {
		BoundValueOperations<Object, Object> ops = redisTemplate.boundValueOps(KEY);
		
		// set string
		ops.set("tom");
		Assertions.assertEquals(ops.get(), "tom");
		redisTemplate.delete(KEY);
		Assertions.assertNull(ops.get());
		
		// set object
		ops.set(info);
		//noinspection unchecked
		Map<String, Object> map = (Map<String, Object>) ops.get();
		Assertions.assertEquals(map.get("username"), "tom");
		Assertions.assertEquals(map.get("age"), 18);
		Assertions.assertEquals(map.get("password"), "111111");
		redisTemplate.delete(KEY);
		Assertions.assertNull(ops.get());
	}
	
	
}
