package com.ooooo.controller;

import com.ooooo.autoconfigure.env.CompositePropertySources;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
	
	@Autowired
	private CompositePropertySources compositePropertySources;
	
	@GetMapping("/form")
	public EncryptedResult<String> hello(String name, Integer age) {
		EncryptedResult<String> success = new EncryptedResult<>();
		success.setData(name + age);
		return success;
	}
	
	
	@GetMapping("/getProperty")
	public Result<String> getProperty(String checkValue) {
		return new Result<>(compositePropertySources.getProperty(checkValue));
	}
	
	
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class EncryptedResult<T> {
		
		private T data;
		
		private String code = "0";
		
		private String message = "OK";
		
		public EncryptedResult(T data) {
			this.data = data;
		}
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
