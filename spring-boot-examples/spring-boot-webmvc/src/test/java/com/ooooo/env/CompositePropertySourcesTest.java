package com.ooooo.env;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ooooo.autoconfigure.env.AbstractSimplePropertySource;
import com.ooooo.autoconfigure.env.CompositePropertySources;
import com.ooooo.controller.TestController.EncryptedResult;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.ooooo.constant.EnvironmentConstants.ENV_PREFIX;
import static com.ooooo.constant.EnvironmentConstants.PROFILE_DEV;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/15 14:12
 * @since 1.0.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = {PROFILE_DEV})
public class CompositePropertySourcesTest {
	
	@Autowired
	private CompositePropertySources compositePropertySources;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getPropertyForConfigurationClass() {
		String value = compositePropertySources.getProperty("type");
		assertEquals("type-value", value);
	}
	
	@Test
	@SneakyThrows
	public void getPropertyForWeb() {
		String value = "1234";
		
		MvcResult mvcResult = mockMvc.perform(request(HttpMethod.GET, "/test/getProperty")
				                                      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
				                                      .param("checkValue", "type")
				                                      .param(ENV_PREFIX + "type", value))
		                             .andReturn();
		
		String contentAsString = mvcResult.getResponse().getContentAsString();
		assertNotNull(contentAsString);
		
		EncryptedResult<String> result = JSON.parseObject(contentAsString, new TypeReference<EncryptedResult<String>>() {});
		assertEquals(value, result.getData());
		
		mvcResult = mockMvc.perform(request(HttpMethod.GET, "/test/getProperty")
				                            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
				                            .param("checkValue", "xxx.yyy")
				                            .param(ENV_PREFIX + "xxx_yyy", value))
		                   .andReturn();
		
		contentAsString = mvcResult.getResponse().getContentAsString();
		assertNotNull(contentAsString);
		
		result = JSON.parseObject(contentAsString, new TypeReference<EncryptedResult<String>>() {});
		assertEquals(value, result.getData());
	}
	
	@Test
	public void getPropertyForLocalFile() {
		//File file = new File("./test.properties");
		//String properties =
	}
	
	
	@TestConfiguration
	public static class TestPropertySourceConfiguration {
		
		@Bean
		public AbstractSimplePropertySource testSimplePropertySource() {
			return new AbstractSimplePropertySource("test") {
				
				@Override
				public Object getProperty(String name) {
					if ("type".equals(name)) {
						return "type-value";
					}
					return null;
				}
			};
		}
		
		
	}
}
