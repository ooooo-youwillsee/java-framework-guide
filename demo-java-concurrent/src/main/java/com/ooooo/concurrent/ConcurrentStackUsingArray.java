package com.ooooo.concurrent;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * CAS 实现 stack
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class ConcurrentStackUsingArray<E> {

  private final AtomicInteger CTL = new AtomicInteger(0);

  private final AtomicReference<E[]> arr = new AtomicReference<>((E[]) new Object[10]);

  private final AtomicInteger index = new AtomicInteger(0);

  public void push(E e) {
    while (!CTL.compareAndSet(0, 1)) {
      Thread.yield();
    }

    while (index.get() >= arr.get().length) {
      E[] oldArr = arr.get();
      E[] newArr = (E[]) new Object[oldArr.length * 2];
      System.arraycopy(oldArr, 0, newArr, 0, oldArr.length);
      if (arr.compareAndSet(oldArr, newArr)) {
        break;
      }
    }

    arr.get()[index.getAndIncrement()] = e;
    CTL.lazySet(0);
  }

  public E pop() {
    while (!CTL.compareAndSet(0, 1)) {
      Thread.yield();
    }

    E e = arr.get()[index.decrementAndGet()];
    CTL.lazySet(0);
    return e;
  }

}
