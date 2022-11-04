package com.ooooo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
@SpringBootApplication
public class TestPropertySourceConfiguration {

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