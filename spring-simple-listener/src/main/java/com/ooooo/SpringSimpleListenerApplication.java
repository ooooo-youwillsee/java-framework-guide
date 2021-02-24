package com.ooooo;

import com.ooooo.annotation.SimpleMulticaster;
import com.ooooo.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
@SpringBootApplication
public class SpringSimpleListenerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(SpringSimpleListenerApplication.class, args);
	}
	
}
