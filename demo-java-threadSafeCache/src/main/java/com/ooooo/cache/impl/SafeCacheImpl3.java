package com.ooooo.cache.impl;

import com.ooooo.cache.Cache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SafeCacheImpl3<T> implements Cache<T> {

  private final Map<String, T> map = new ConcurrentHashMap<>();

  @Override
  public T get(String key) {
    return map.get(key);
  }

  @Override
  public void put(String key, Supplier<T> supplier) {
    // 每次都会计算 supplier.get(), 导致重复计算
    // map.putIfAbsent(key, supplier.get());

    // 存在竞态条件，所有会导致 supplier.get() 会被重复计算, value 值不一样
    // if (!map.containsKey(key)) {
    //   map.put(key, supplier.get());
    // }

    // 最好的做法
    map.computeIfAbsent(key, __ -> supplier.get());
  }
}
