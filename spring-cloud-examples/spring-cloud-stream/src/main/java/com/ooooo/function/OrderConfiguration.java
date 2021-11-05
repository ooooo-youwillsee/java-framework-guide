package com.ooooo.function;

import com.alibaba.fastjson.JSON;
import com.ooooo.config.DisableSpringCloudStreamConfiguration;
import com.ooooo.dao.entity.Order;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author ooooo
 * @date 2021/09/21 15:08
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class OrderConfiguration implements EnvironmentAware {

	private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();

	@Setter
	private Environment environment;

	/**
	 * push order info to stream
	 */
	@Bean
	Supplier<Order> generateOrder() {
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
	Consumer<Order> insertOrder() {
		return order -> {
			log.info("insert order: {}", JSON.toJSONString(order));
		};
	}


	public void putMessage(Order order) {
		if (DisableSpringCloudStreamConfiguration.disabled(environment)) {
			log.info("spring cloud stream is disabled, not send messaage: {}", JSON.toJSONString(order));
		} else {
			orderQueue.offer(order);
		}
	}

}
