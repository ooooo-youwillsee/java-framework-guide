package com.ooooo.config;

import com.ooooo.annotation.XRequestMappingHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/24 16:59
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	
	@Bean
	public HandlerMapping xRequestMappingHandlerMapping() {
		return new XRequestMappingHandlerMapping();
	}
			
	
}
