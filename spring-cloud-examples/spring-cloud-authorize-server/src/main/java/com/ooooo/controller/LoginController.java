package com.ooooo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/6/7 13:14
 */
@Controller
public class LoginController {
	
	@RequestMapping("/loginPage")
	public String loginPage() {
		return "login";
	}
}
