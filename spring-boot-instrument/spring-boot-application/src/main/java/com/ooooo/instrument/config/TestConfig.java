package com.ooooo.instrument.config;

import com.ooooo.instrument.service.TestService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class TestConfig {

  @Autowired
  private TestService testService;


  @Bean
  public ApplicationRunner applicationRunner() {
    return __ -> {
      new Thread(() -> {

        while (true) {
          // test static method
          pauseSecond(2);
          String res1 = TestService.test1();
          log.info("test: {}", res1);

          // test instance method
          pauseSecond(2);
          String res2 = testService.test2();
          log.info("test: {}", res2);

        }
      }).start();
    };
  }


  private void pauseSecond(long second) {
    try {
      TimeUnit.SECONDS.sleep(second);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
