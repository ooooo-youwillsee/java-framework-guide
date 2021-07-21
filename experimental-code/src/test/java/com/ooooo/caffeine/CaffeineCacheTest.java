package com.ooooo.caffeine;

import com.github.benmanes.caffeine.cache.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author ooooo
 * @date 2021/07/21 20:15
 */
public class CaffeineCacheTest {

	private static final String TEST_KEY = "test_key";

	@Test
	public void testCaffeineCache() {
		Cache<String, String> cache = Caffeine.newBuilder()
						.expireAfterWrite(1, TimeUnit.SECONDS)
						.expireAfterAccess(1, TimeUnit.SECONDS)
						.maximumSize(10_000)
						.build();

		Assertions.assertNull(cache.getIfPresent(TEST_KEY));
		cache.put(TEST_KEY, TEST_KEY);
		Assertions.assertEquals(TEST_KEY, cache.getIfPresent(TEST_KEY));

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignored) {
		}
		Assertions.assertNull(cache.getIfPresent(TEST_KEY));

		cache.put(TEST_KEY, TEST_KEY);
		cache.invalidate(TEST_KEY);
		Assertions.assertNull(cache.getIfPresent(TEST_KEY));
	}

	@Test
	public void testCaffeineCacheWithCacheLoader() {
		LoadingCache<String, String> cache = Caffeine.newBuilder()
						.expireAfterWrite(1, TimeUnit.SECONDS)
						.expireAfterAccess(1, TimeUnit.SECONDS)
						.maximumSize(10_000)
						.build(k -> TEST_KEY.equals(k) ? TEST_KEY : null);

		Assertions.assertEquals(TEST_KEY, cache.get(TEST_KEY));
		Assertions.assertNull(cache.getIfPresent("123"));
	}


	@Test
	public void testCaffeineCacheAsync() throws ExecutionException, InterruptedException {
		AsyncCache<String, String> cache = Caffeine.newBuilder()
						.expireAfterWrite(1, TimeUnit.SECONDS)
						.expireAfterAccess(1, TimeUnit.SECONDS)
						.maximumSize(10_000)
						.buildAsync();

		Assertions.assertNull(cache.getIfPresent(TEST_KEY));
		cache.put(TEST_KEY, CompletableFuture.supplyAsync(() -> TEST_KEY));
		Assertions.assertEquals(TEST_KEY, cache.getIfPresent(TEST_KEY).get());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException ignored) {
		}
		Assertions.assertNull(cache.getIfPresent(TEST_KEY));

		cache.put(TEST_KEY, CompletableFuture.supplyAsync(() -> TEST_KEY));
		cache.synchronous().invalidate(TEST_KEY);
		Assertions.assertNull(cache.getIfPresent(TEST_KEY));
	}


	@Test
	public void testCaffeineCacheWithCacheLoaderAsync() throws ExecutionException, InterruptedException {
		AsyncLoadingCache<String, String> cache = Caffeine.newBuilder()
						.expireAfterWrite(1, TimeUnit.SECONDS)
						.expireAfterAccess(1, TimeUnit.SECONDS)
						.maximumSize(10_000)
						.buildAsync(k -> TEST_KEY.equals(k) ? TEST_KEY : null);

		Assertions.assertEquals(TEST_KEY, cache.get(TEST_KEY).get());
		Assertions.assertNull(cache.getIfPresent("123"));
	}
}
