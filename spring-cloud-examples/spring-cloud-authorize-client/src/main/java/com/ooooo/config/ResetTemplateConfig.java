package com.ooooo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/3/31 15:22
 */
@Configuration
public class ResetTemplateConfig {

    private final ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory() {
        @Override
		protected void prepareConnection(HttpURLConnection connection, String httpMethod)
				throws IOException {
			super.prepareConnection(connection, httpMethod);
			connection.setInstanceFollowRedirects(false);
			connection.setUseCaches(false);
		}
	};
	
	
	@Bean
	public RestTemplate oauth2RestTemplate() {
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}
}
