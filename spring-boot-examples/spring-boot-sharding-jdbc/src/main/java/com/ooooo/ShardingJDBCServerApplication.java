package com.ooooo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author leizhijie
 * @since 2021/3/12 18:31
 */
@MapperScan(basePackages = "com.ooooo")
//@SpringBootApplication(exclude = {
//				JtaAutoConfiguration.class,
//				DataSourceAutoConfiguration.class
//})
@SpringBootApplication
public class ShardingJDBCServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShardingJDBCServerApplication.class, args);
	}

}
