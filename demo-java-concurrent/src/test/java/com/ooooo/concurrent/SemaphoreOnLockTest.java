package com.ooooo.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
class SemaphoreOnLockTest {

  @SneakyThrows
  @Test
  void test() {
    AtomicInteger count = new AtomicInteger(0);
    SemaphoreOnLock lock = new SemaphoreOnLock(3);
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    for (int i = 0; i < 100; i++) {
      executorService.submit(() -> {
        lock.acquire();

        System.out.println(count.getAndIncrement() + ":" + Thread.currentThread().getName());

        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ignored) {
        }

        lock.release();
      });
    }

    new CountDownLatch(1).await();
  }
}