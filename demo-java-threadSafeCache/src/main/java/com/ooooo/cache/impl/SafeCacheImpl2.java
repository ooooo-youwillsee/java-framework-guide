package com.ooooo.cache.impl;

import com.ooooo.cache.Cache;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SafeCacheImpl2<T> implements Cache<T> {

  private final Map<String, T> map = new HashMap<>();

  @Override
  public synchronized T get(String key) {
    return map.get(key);
  }

  /**
   * 因为使用 synchronized, 导致性能太低了
   * 比如 A -> AValue, B -> BValue, 这两个操作应该是互不影响的，
   * 但是现在锁是同一个，所以性能不高
   *
   * @param key
   * @param supplier
   */
  @Override
  public void put(String key, Supplier<T> supplier) {
    if (!map.containsKey(key)) {
      synchronized (this) {
        if (!map.containsKey(key)) {
          map.put(key, supplier.get());
        }
      }
    }
  }
}
