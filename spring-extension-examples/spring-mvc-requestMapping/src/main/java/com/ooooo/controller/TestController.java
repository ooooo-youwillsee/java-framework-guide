package com.ooooo.controller;

import com.ooooo.annotation.XRequestMapping;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @since 2021/2/24 15:21
 */
@XRequestMapping("/test")
@RestController
public class TestController {
	
	@XRequestMapping("/hello")
	public String hello() {
		return "hello";
	}
	
	
	@XRequestMapping(value = "/helloPost", method = HttpMethod.POST)
	public String helloPost() {
		return "helloPost";
	}
}
