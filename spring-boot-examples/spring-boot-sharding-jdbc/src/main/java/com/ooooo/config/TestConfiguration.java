package com.ooooo.config;

import com.ooooo.mapper.OrderItemMapper;
import com.ooooo.mapper.OrderMapper;
import com.ooooo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/01 06:53
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class TestConfiguration {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;


	@Bean
	public ApplicationRunner createTablesApplicationRunner() {
		return __ -> {
			userMapper.createTable();
			orderMapper.createTable();
			orderItemMapper.createTable();

			log.info("createTablesApplicationRunner success!!!");
		};
	}
}
