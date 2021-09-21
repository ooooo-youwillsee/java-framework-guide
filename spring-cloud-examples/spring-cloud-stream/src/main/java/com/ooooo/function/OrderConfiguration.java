package com.ooooo.function;

import com.alibaba.fastjson.JSON;
import com.ooooo.entity.Order;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author ooooo
 * @date 2021/09/21 15:08
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class OrderConfiguration {

	@Getter
	private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

	/**
	 * push order info to stream
	 */
	@Bean
	public Supplier<Order> generateOrder() {
		return () -> {
			try {
				Order order = orderQueue.take();
				log.info("send order: {}", JSON.toJSONString(order));
				return order;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		};
	}


	/**
	 * pull order info from stream
	 */
	@Bean
	public Consumer<Order> insertOrder() {
		return order -> {
			log.info("insert order: {}", JSON.toJSONString(order));
		};
	}
}
