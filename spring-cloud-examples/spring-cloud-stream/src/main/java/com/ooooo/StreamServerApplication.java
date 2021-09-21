package com.ooooo;

import com.ooooo.entity.Order;
import com.ooooo.function.OrderConfiguration;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;

/**
 * @author leizhijie
 * @since 2021/3/12 18:31
 */
@SpringBootApplication
public class StreamServerApplication {

	@SneakyThrows
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(StreamServerApplication.class, args);

		OrderConfiguration orderConfiguration = context.getBean(OrderConfiguration.class);
		Thread.sleep(5000);

		for (int i = 10; i < 20; i++) {
			Order order = new Order();
			order.setId(System.currentTimeMillis());
			order.setUserId(String.valueOf(i));
			order.setPayDate(new Date());

			orderConfiguration.getOrderQueue().offer(order);
		}
	}

}
