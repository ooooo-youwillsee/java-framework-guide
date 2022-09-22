package com.ooooo.service;

import com.ooooo.ClassLoaderApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@SpringBootTest(classes = ClassLoaderApplication.class)
class TestServiceTest {

  @Autowired
  private TestService testService;

  @Test
  void test1() {
    String result = testService.test1("123");

    assertEquals("module-a: 123", result);
  }


  @Test
  void test2() {
    String result = testService.test2("123");

    assertEquals("module-b: 123", result);
  }
}