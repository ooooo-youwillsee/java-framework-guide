package com.ooooo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/12 18:31
 */
@MapperScan(basePackages = "com.ooooo")
@SpringBootApplication
public class ShardingJDBCServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingJDBCServerApplication.class, args);
	}

}
