package com.ooooo.juc;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用 {@link ReentrantLock} 来实现 {@link Semaphore}
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SemaphoreOnLock {

  private final ReentrantLock lock = new ReentrantLock();

  private final Condition condition = lock.newCondition();

  private int permit;

  public SemaphoreOnLock(int permit) {
    this.permit = permit;
  }

  /**
   * 获取锁
   */
  public void acquire() {
    lock.lock();
    try {
      while (permit <= 0) {
        condition.await();
      }
      permit--;
    } catch (InterruptedException ignored) {

    } finally {
      lock.unlock();
    }
  }

  public void release() {
    lock.lock();
    try {
      permit++;
      condition.signal();
    } finally {
      lock.unlock();
    }
  }

}
