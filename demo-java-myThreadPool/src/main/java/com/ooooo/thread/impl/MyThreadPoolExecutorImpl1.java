package com.ooooo.thread.impl;

import com.ooooo.thread.MyFuture;
import com.ooooo.thread.MyThreadPoolExecutor;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 单一的线程池
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class MyThreadPoolExecutorImpl1 implements MyThreadPoolExecutor {

  private volatile Worker worker;

  private final LinkedBlockingQueue<Runnable> blockingQueue;

  public MyThreadPoolExecutorImpl1() {
    this.blockingQueue = new LinkedBlockingQueue<>();
  }

  @Override
  public void execute(Runnable runnable) {
    startIfNecessary();
    blockingQueue.offer(runnable);
  }

  @Override
  public <T> MyFuture<T> submit(Callable<T> callable) {
    startIfNecessary();
    MyRunnableFuture<T> future = new MyRunnableFuture<>(callable);
    blockingQueue.offer(future);
    return future;
  }


  private void startIfNecessary() {
    if (worker == null) {
      synchronized (this) {
        if (worker == null) {
          worker = new Worker();
          worker.thread.start();
        }
      }
    }
  }

  private class Worker implements Runnable {

    private final Thread thread;

    public Worker() {
      this.thread = new Thread(this);
    }

    @Override
    public void run() {
      while (!thread.isInterrupted()) {
        Runnable runnable = null;
        try {
          runnable = blockingQueue.take();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

        if (runnable != null) {
          runnable.run();
        }
      }
    }
  }


}
