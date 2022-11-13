package com.ooooo.juc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
class ConcurrentStackUsingArrayTest {

  @SneakyThrows
  @Test
  void test() {
    AtomicInteger cnt = new AtomicInteger(0);
    AtomicInteger sum = new AtomicInteger(0);
    AtomicInteger expectedSum = new AtomicInteger(0);
    ConcurrentStackUsingArray<Integer> stack = new ConcurrentStackUsingArray<>();
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    CountDownLatch countDownLatch = new CountDownLatch(1000);
    for (int i = 0; i < 1000; i++) {
      executorService.submit(() -> {
        stack.push(cnt.getAndIncrement());
        sum.addAndGet(stack.pop());
        countDownLatch.countDown();
      });
      expectedSum.addAndGet(i);
    }

    countDownLatch.await();

    assertEquals(expectedSum.get(), sum.get());
  }


}