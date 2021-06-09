package com.ooooo.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author ooooo
 * @since 2021/06/09 17:59
 */
@Configuration
public class TestOrderConfiguration1 {

	@Bean
	@Order(2)
	public IHello aHello() {
		return new Hello("2");
	}


	@Order
	@Bean
	public IHello dHello() {
		return new Hello("5");
	}


	@Order(4)
	@Bean
	public IHello bHello() {
		return new Hello("4");
	}
}


