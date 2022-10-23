package com.ooooo.cache.impl;

import com.ooooo.cache.Cache;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SafeCacheImpl4<T> implements Cache<T> {

  private final Map<String, ValueHolder<T>> map = new ConcurrentHashMap<>();

  @Override
  public T get(String key) {
    return map.get(key).get();
  }

  @Override
  public void put(String key, Supplier<T> supplier) {
    if (map.containsKey(key)) {
      return;
    }

    ValueHolder<T> value = new ValueHolder<>(supplier);
    ValueHolder<T> targetValue = map.putIfAbsent(key, value);
    if (targetValue == null) {
      targetValue = value;
      // 初始化调用
      targetValue.get();
    }
  }

  private static class ValueHolder<T> {

    private final AtomicBoolean init = new AtomicBoolean(false);

    private volatile T value;

    private Supplier<T> supplier;

    private ValueHolder(Supplier<T> supplier) {
      this.supplier = supplier;
    }

    T get() {
      if (init.compareAndSet(false, true)) {
        value = supplier.get();
        supplier = null;
      }

      return value;
    }
  }
}
