package com.ooooo.rest;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * @author ooooo
 * @since 6/7/2021 21:11
 */
@Slf4j
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
		ClientHttpRequestFactory clientHttpRequestFactory = new OkHttp3ClientHttpRequestFactory();
		RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory);
		
		restTemplate.getInterceptors().add((request, body, execution) -> {
			log.info(new String(body, StandardCharsets.UTF_8));
			return execution.execute(request, body);
		});
		// message converter setting
		for (HttpMessageConverter<?> messageConverter : restTemplate.getMessageConverters()) {
			if (messageConverter instanceof StringHttpMessageConverter) {
				((StringHttpMessageConverter) messageConverter).setDefaultCharset(StandardCharsets.UTF_8);
			} else if (messageConverter instanceof MappingJackson2HttpMessageConverter) {
				ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) messageConverter).getObjectMapper();
				objectMapper.setSerializationInclusion(Include.NON_NULL);
			}
		}
		return restTemplate;
	}
}
