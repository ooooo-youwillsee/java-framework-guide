package com.ooooo.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class ThreadPoolExecutorTest {
	
	@Test
	void testShutdown() {
		AtomicInteger cnt = new AtomicInteger(0);
		ExecutorService executorService = Executors.newFixedThreadPool(3);
		
		for (int i = 0; i < 10; i++) {
			executorService.submit(() -> {
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				System.out.println("cnt: " + cnt.incrementAndGet());
			});
		}
		
		executorService.shutdown();
		
		try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	@Test
	void testExceptionally() {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		// normal execute
		executorService.submit(() -> {
			System.out.println("--------1----------");
			System.out.println("--------2----------");
		});
		
		// it doesn't throw an exception
		executorService.submit(() -> {
			System.out.println("--------3----------");
			int a = 1 / 0;
			System.out.println("--------4----------");
		});
		
		executorService.submit(() -> {
			System.out.println("--------5----------");
			try {
				int a = 1 / 0;
			} catch (Exception e) {
				System.out.println(e);
			}
			System.out.println("--------6----------");
		});
		
		try {
			new CountDownLatch(1).await();
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}
