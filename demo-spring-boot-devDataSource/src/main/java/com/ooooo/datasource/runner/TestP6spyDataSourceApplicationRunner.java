package com.ooooo.datasource.runner;

import com.ooooo.datasource.dao.entity.User;
import com.ooooo.datasource.dao.mapper.UserMapper;
import com.ooooo.datasource.service.ServiceA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/5 18:23
 * @since 1.0.0
 */
@Slf4j
@Component
public class TestP6spyDataSourceApplicationRunner implements ApplicationRunner {

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private ServiceA serviceA;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    userMapper.createTable();
    userMapper.insert(new User(1L, "jack", "123456"));

    try {
      serviceA.testA();
    } catch (Throwable ignored) {
    }

    List<User> users = userMapper.selectList(null);
    for (User user : users) {
      log.info("user info: {}", user);
    }
  }
}
