package com.ooooo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class TestClientImpl implements TestClient {

  @Async
  public String sayHello() {
    return "Hello";
  }
}
