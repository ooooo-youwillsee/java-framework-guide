package com.ooooo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class HelloService {

  public static String test2(String message) {
    log.info("module-b, say: {}", message);

    return "module-b: " + message;
  }

}
