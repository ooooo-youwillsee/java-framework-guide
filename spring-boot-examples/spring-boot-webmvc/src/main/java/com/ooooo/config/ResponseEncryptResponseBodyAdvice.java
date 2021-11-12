package com.ooooo.config;

import com.alibaba.fastjson.JSON;
import com.ooooo.controller.TestController.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.ooooo.util.EncryptUtil.aesEncryptByCBC;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/12 11:18
 * @since 1.0.0
 */
@Slf4j
@RestControllerAdvice
public class ResponseEncryptResponseBodyAdvice implements ResponseBodyAdvice<Object> {
	
	public static final String ENCRYPT_KEY = "IymNg3mGl6RI5G72";
	
	public static final String SPLIT_SEPARATOR = "===";
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
	}
	
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		
		if (body instanceof Result) {
			String content = JSON.toJSONString(body);
			String encryptContent = null;
			try {
				encryptContent = aesEncryptByCBC(content, ENCRYPT_KEY);
				encryptContent = encryptContent + SPLIT_SEPARATOR + encryptContent.length();
			} catch (Exception e) {
				log.error("aesEncryptByCBC error, content: {}", content);
				encryptContent = content;
			}
			
			body = new Result<>(encryptContent);
		}
		
		return body;
	}
}
