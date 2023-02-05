package com.ooooo.thread;

import java.util.concurrent.Callable;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface MyThreadPoolExecutor {
  
  void execute(Runnable runnable);
  
  <T> MyFuture<T> submit(Callable<T> callable);
  
  void shutdown();
}
