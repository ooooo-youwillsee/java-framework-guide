package com.ooooo.cache;

import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface Cache<T> {

  T get(String key);

  void put(String key, Supplier<T> supplier);
}
