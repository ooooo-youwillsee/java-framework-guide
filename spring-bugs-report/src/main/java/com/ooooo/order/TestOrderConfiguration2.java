package com.ooooo.order;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ooooo
 * @since 2021/06/09 17:59
 */
@Configuration
public class TestOrderConfiguration2 {

	@Bean
	public IHello cHello() {
		return new HelloImpl("3");
	}

}


