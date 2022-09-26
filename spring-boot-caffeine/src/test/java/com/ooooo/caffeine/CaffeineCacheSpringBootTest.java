package com.ooooo.caffeine;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ooooo.caffeine.CaffeineCacheSpringBootTest.Application;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = Application.class)
public class CaffeineCacheSpringBootTest {

  @Autowired
  private CacheManager cacheManager;

  @Test
  public void test() {
    assertTrue(cacheManager instanceof CaffeineCacheManager, "cacheManager instanceof CaffeineCacheManager");
  }


  @SpringBootApplication
  @EnableCaching
  public static class Application {}
}
