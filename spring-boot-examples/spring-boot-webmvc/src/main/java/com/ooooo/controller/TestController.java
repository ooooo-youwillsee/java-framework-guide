package com.ooooo.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/12 11:01
 * @since 1.0.0
 */
@RestController
@RequestMapping("/test")
public class TestController {
	
	
	@GetMapping("/form")
	public Result<String> hello(String name, Integer age) {
		Result<String> success = new Result<>();
		success.setData(name + age);
		return success;
	}
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Result<T> {
		
		private T data;
		
		private String code = "0";
		
		private String message = "OK";
		
		public Result(T data) {
			this.data = data;
		}
	}
}
