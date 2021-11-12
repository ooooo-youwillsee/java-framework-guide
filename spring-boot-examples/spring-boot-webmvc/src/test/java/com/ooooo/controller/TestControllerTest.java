package com.ooooo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ooooo.controller.TestController.Result;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.ooooo.config.ResponseEncryptResponseBodyAdvice.ENCRYPT_KEY;
import static com.ooooo.config.ResponseEncryptResponseBodyAdvice.SPLIT_SEPARATOR;
import static com.ooooo.util.EncryptUtil.aesDecryptByCBC;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/12 13:35
 * @since 1.0.0
 */
@AutoConfigureMockMvc
@SpringBootTest
class TestControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@SneakyThrows
	@ParameterizedTest
	@MethodSource("helloParams")
	void hello(String name, Integer age) {
		
		MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/test/form")
				                                      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
				                                      .param("name", name)
				                                      .param("age", String.valueOf(age)))
		                             .andReturn();
		
		String contentAsString = mvcResult.getResponse().getContentAsString();
		assertNotNull(contentAsString);
		
		Result<String> encryptedResult = JSON.parseObject(contentAsString, new TypeReference<Result<String>>() {});
		assertNotNull(encryptedResult);
		String encryptedData = encryptedResult.getData();
		assertNotNull(encryptedData);
		
		int i = encryptedData.indexOf(SPLIT_SEPARATOR);
		
		String decryptedContent = encryptedData;
		if (i >= 0) {
			int len = Integer.parseInt(encryptedData.substring(i + SPLIT_SEPARATOR.length()));
			String encryptedContent = encryptedData.substring(0, len);
			assertNotNull(encryptedContent);
			decryptedContent = aesDecryptByCBC(encryptedContent, ENCRYPT_KEY);
			assertNotNull(decryptedContent);
		}
		
		Result<String> decryptedResult = JSON.parseObject(decryptedContent, new TypeReference<Result<String>>() {});
		assertEquals(name + age, decryptedResult.getData());
		
	}
	
	static Stream<Object[]> helloParams() {
		return Stream.of(new Object[]{"tom", 18}, new Object[]{"jerry", 20});
	}
}