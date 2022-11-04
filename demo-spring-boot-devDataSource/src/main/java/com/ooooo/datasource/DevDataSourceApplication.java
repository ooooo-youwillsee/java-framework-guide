package com.ooooo.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/5 18:01
 * @since 1.0.0
 */
@MapperScan("com.ooooo.**.mapper")
@SpringBootApplication
public class DevDataSourceApplication {

  public static void main(String[] args) {
    SpringApplication.run(DevDataSourceApplication.class, args);
  }
}
