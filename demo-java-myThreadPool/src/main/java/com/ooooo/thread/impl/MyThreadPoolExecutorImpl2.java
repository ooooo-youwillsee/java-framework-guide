package com.ooooo.thread.impl;

import com.ooooo.thread.MyFuture;
import com.ooooo.thread.MyThreadPoolExecutor;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 多个的线程池
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class MyThreadPoolExecutorImpl2 implements MyThreadPoolExecutor {
	
	private volatile List<Worker> workers;
	
	private final LinkedBlockingQueue<Runnable> blockingQueue;
	
	private final int coreSize;
	
	private final int maxSize;
	
	private final int queueSize;
	
	public MyThreadPoolExecutorImpl2(int coreSize, int maxSize, int queueSize) {
		this.coreSize = coreSize;
		this.maxSize = maxSize;
		this.blockingQueue = new LinkedBlockingQueue<>(queueSize);
		this.queueSize = queueSize;
	}
	
	@Override
	public void execute(Runnable runnable) {
		if (workers == null) {
			synchronized (this) {
				if (workers == null) {
					workers = new LinkedList<>();
				}
			}
		}
		
		if (workers.size() < coreSize) {
			Worker w = new Worker();
			w.thread.start();
			workers.add(w);
		} else {
			if (!blockingQueue.offer(runnable)) {
				if (workers.size() < maxSize) {
					Worker w = new Worker();
					w.thread.start();
					workers.add(w);
				}
			}
		}
	}
	
	@Override
	public <T> MyFuture<T> submit(Callable<T> callable) {
		MyRunnableFuture<T> future = new MyRunnableFuture<>(callable);
		execute(future);
		return future;
	}
	
	@Override
	public void shutdown() {
	
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
