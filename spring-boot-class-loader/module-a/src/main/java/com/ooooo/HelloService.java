package com.ooooo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class HelloService {

  public static String test1(String message) {
    log.info("module-a, say: {}", message);

    return "module-a: " + message;
  }
}
