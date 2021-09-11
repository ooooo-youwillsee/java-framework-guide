package com.ooooo.repository;

import com.ooooo.RedisApplicationTests;
import com.ooooo.dao.UserRedisRepository;
import com.ooooo.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

/**
 * @author leizhijie
 * @since 2021/4/12 21:54
 * <p>
 * RedisRepository 操作
 */
public class RepositoryOperationTests extends RedisApplicationTests {

	private User user;

	@Autowired
	private UserRedisRepository userRedisRepository;

	@BeforeEach
	public void before() {
		user = new User();
		user.setId(1L);
		user.setUsername("tom");
		user.setAge(18);
		user.setAddress("xxxx");
	}


	@Test
	public void insertAndDelete() {
		// save
		userRedisRepository.save(user);

		User findUser = userRedisRepository.findById(this.user.getId()).orElse(null);
		Assertions.assertEquals(findUser, user);

		// redis 内部存储: key为"user" 的 set 数据结构, 存储都是id
		Iterable<User> allUsers = userRedisRepository.findAll();
		Assertions.assertIterableEquals(allUsers, Collections.singleton(user));

		// test for delete
		userRedisRepository.deleteById(user.getId());
		findUser = userRedisRepository.findById(this.user.getId()).orElse(null);
		Assertions.assertNull(findUser);

		userRedisRepository.deleteAll();
		allUsers = userRedisRepository.findAll();
		Assertions.assertEquals(allUsers, Collections.emptyList());
	}

}
