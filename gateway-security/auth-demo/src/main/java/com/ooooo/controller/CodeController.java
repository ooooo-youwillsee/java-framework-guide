package com.ooooo.controller;

import java.security.Principal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @since 2021/3/15 10:02
 */
@RestController
public class CodeController {
	
	@GetMapping("/oauth/code")
	public String getAuthorizationCode(String code, String state) {
		return String.format("code : %s, state: %s", code, state);
	}
	
	@GetMapping("/test")
	public String test(Principal principal) {
		return "test";
	}
}
