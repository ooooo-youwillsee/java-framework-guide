package com.ooooo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
public class TestConfiguration {

//  @Bean
//  public TestClientImpl testClient2() {
//    return testClient();
//  }

  @Bean
  public TestClientImpl testClient() {
    return new TestClientImpl();
  }

}
