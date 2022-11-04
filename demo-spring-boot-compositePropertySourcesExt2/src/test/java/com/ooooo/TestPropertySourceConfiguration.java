package com.ooooo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@SpringBootApplication
public class TestPropertySourceConfiguration {

  @Bean
  public AbstractSimplePropertySource test1SimplePropertySource() {
    return new AbstractSimplePropertySource("test1") {

      @Override
      public Object getProperty(String name) {
        if ("tom".equals(name)) {
          return "123";
        }

        return null;
      }
    };
  }

  @Bean
  public AbstractSimplePropertySource test2SimplePropertySource() {
    return new AbstractSimplePropertySource("test2") {

      @Override
      public Object getProperty(String name) {
        if ("businType.tom".equals(name)) {
          return "456";
        }

        return null;
      }
    };
  }

}