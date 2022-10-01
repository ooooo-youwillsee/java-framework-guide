package com.ooooo.instrument.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@Component
public class TestService {

  public static String test1() {
    return "test1";
  }


  public String test2() {
    return "test2";
  }


}
