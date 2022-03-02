package com.ooooo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
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
