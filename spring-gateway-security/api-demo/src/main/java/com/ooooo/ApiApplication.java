package com.ooooo;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @since 2021/3/12 18:31
 */
@SpringBootApplication
@RestController
@Slf4j
public class ApiApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}
	
	@GetMapping("/test")
	public String test(HttpServletRequest request) {
		log.info("==========test=============");
		String authorization = request.getHeader("authorization");
		return "test";
	}
}
