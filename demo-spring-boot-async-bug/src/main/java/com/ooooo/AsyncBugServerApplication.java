package com.ooooo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/10 16:36
 * @since 1.0.0
 */
@EnableAsync
@SpringBootApplication
public class AsyncBugServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncBugServerApplication.class, args);
	}
}
