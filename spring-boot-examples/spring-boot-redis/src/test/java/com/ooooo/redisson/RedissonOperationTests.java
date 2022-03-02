package com.ooooo.redisson;

import com.ooooo.RedisApplicationTests;
import com.ooooo.constant.ProfileConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/4/13 20:32
 */
@ActiveProfiles(ProfileConstants.PROFILE_REDISSON)
public class RedissonOperationTests extends RedisApplicationTests {

	private static final String KEY = "redisson-key";
	private final Logger log = LoggerFactory.getLogger(RedissonOperationTests.class);
	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;
	@Autowired
	private RedissonClient redissonClient;

	@Test
	public void insertAndUpate() {
		BoundValueOperations<Object, Object> ops = redisTemplate.boundValueOps(KEY);

		ops.set("123456");
		Assertions.assertEquals(ops.get(), "123456");
		redisTemplate.delete(KEY);
		Assertions.assertNull(ops.get());
		Assertions.assertFalse(redisTemplate.hasKey(KEY));
	}

	/**
	 * JUC
	 */
	@Test
	public void JUCOperations() {

		// distributed map operations
		RMap<String, String> map = redissonClient.getMap(KEY);
		Assertions.assertNull(map.get("tom"));
		map.put("tom", "123");
		Assertions.assertEquals(map.get("tom"), "123");
		map.remove("tom");
		Assertions.assertNull(map.get("tom"));

		// ....
	}


	@Test
	public void distributedLock() {
		new Thread(() -> {
			RLock lock = redissonClient.getLock(KEY);
			try {
				// 30秒内获取锁，返回true，并且在20秒后自动释放，如果过了等待时间没有获取锁，就会返回false
				boolean locked = lock.tryLock(30, 20, TimeUnit.SECONDS);
				if (locked) {
					log.info("{} 获取锁成功", Thread.currentThread().getName());
				} else {
					log.info("{} 获取锁失败", Thread.currentThread().getName());
				}

				//if (locked) {
				//	lock.unlock();
				//	log.info("{} 释放锁成功", Thread.currentThread().getName());
				//}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "thread-1").start();

		new Thread(() -> {
			RLock lock = redissonClient.getLock(KEY);
			try {
				// 让线程二后执行
				Thread.sleep(3000);
				// 5秒内获取锁，返回true，并且在20秒后自动释放，如果过了等待时间没有获取锁，就会返回false
				boolean locked = lock.tryLock(5, 20, TimeUnit.SECONDS);
				if (locked) {
					log.info("{} 获取锁成功", Thread.currentThread().getName());
				} else {
					log.info("{} 获取锁失败", Thread.currentThread().getName());
				}

				if (locked) {
					lock.unlock();
					log.info("{} 释放锁成功", Thread.currentThread().getName());
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}, "thread-2").start();

		CountDownLatch countDownLatch = new CountDownLatch(1);
		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


}
