package com.ooooo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用于测试sso
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/6/7 18:08
 */
@RestController
public class HomeController {
	
	@GetMapping("/")
	public String index() {
		return "index";
	}
}
