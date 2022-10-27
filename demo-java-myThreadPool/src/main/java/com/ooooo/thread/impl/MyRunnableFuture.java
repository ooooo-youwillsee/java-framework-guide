package com.ooooo.thread.impl;

import com.ooooo.thread.MyFuture;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class MyRunnableFuture<T> implements MyFuture<T>, Runnable {

  private final Callable<T> callable;

  private volatile T result;

  private volatile Throwable throwable;

  private final CountDownLatch countDownLatch = new CountDownLatch(1);

  public MyRunnableFuture(Callable<T> callable) {
    this.callable = callable;
  }

  @Override
  public T get() {
    try {
      countDownLatch.await();
    } catch (InterruptedException ignored) {
    }

    return result;
  }

  @Override
  public void run() {
    try {
      result = callable.call();
    } catch (Exception t) {
      throwable = t;
    } finally {
      countDownLatch.countDown();
    }
  }
}
