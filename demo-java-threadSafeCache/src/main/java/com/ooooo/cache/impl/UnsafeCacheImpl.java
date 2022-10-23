package com.ooooo.cache.impl;

import com.ooooo.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class UnsafeCacheImpl<T> implements Cache<T> {

  private final Map<String, T> map = new HashMap<>();

  @Override
  public T get(String key) {
    return map.get(key);
  }

  /**
   * 两个线程会同时去执行 put 方法，所以线程不安全
   *
   * @param key
   * @param supplier
   */
  @Override
  public void put(String key, Supplier<T> supplier) {
    if (!map.containsKey(key)) {
      // 两个线程都会执行这里，所以线程不安全
      map.put(key, supplier.get());
    }
  }
}
