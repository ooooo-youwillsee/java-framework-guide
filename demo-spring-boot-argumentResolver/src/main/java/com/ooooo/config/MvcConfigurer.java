package com.ooooo.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 2021/2/22 21:56
 */
@Configuration
public class MvcConfigurer implements WebMvcConfigurer {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(0, xRequestBodyArgumentResolver(null, null));
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(xRequestBodyHandlerInterceptor(null));
  }

  @Bean
  public XRequestBodyArgumentResolver xRequestBodyArgumentResolver(List<HttpMessageConverter<?>> converters, ConversionService conversionService) {
    return new XRequestBodyArgumentResolver(converters, conversionService);
  }

  @Bean
  public XRequestBodyHandlerInterceptor xRequestBodyHandlerInterceptor(MappingJackson2HttpMessageConverter messageConverter) {
    return new XRequestBodyHandlerInterceptor(messageConverter);
  }
}
