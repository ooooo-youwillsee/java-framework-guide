package com.ooooo.autoconfigure;

import java.io.IOException;
import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

/**
 * @author leizhijie
 * @since 2021/2/22 17:06
 */
@Configuration
public class XXXServiceAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public DefaultXXXServiceProperties defaultXXXServiceProperties() {
		return new DefaultXXXServiceProperties();
	}
	
	@Bean
	@ConditionalOnMissingBean(name = "xxxRestTemplate")
	public RestTemplate xxxRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(30 * 1000);
		requestFactory.setReadTimeout(30 * 1000);
		
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		restTemplate.setInterceptors(Collections.singletonList((request, body, execution) -> {
			request.getHeaders().setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
			return execution.execute(request, body);
		}));
		
		restTemplate.setErrorHandler(new ResponseErrorHandler() {
			@Override
			public boolean hasError(ClientHttpResponse response) throws IOException {
				return false;
			}
			
			@Override
			public void handleError(ClientHttpResponse response) throws IOException {
			
			}
		});
		return restTemplate;
	}
	
	
	@Bean
	@ConditionalOnMissingBean
	public DefaultXXXServiceFactory defaultXXXServiceFactory() {
		return new DefaultXXXServiceFactory(defaultXXXServiceProperties(), xxxRestTemplate());
	}
}
