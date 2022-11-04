package com.ooooo.datasource.runner;

import com.ooooo.datasource.dao.entity.User;
import com.ooooo.datasource.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/5 18:23
 * @since 1.0.0
 */
@Component
public class TestP6spyDataSourceApplicationRunner implements ApplicationRunner {

  @Autowired
  private UserMapper userMapper;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    userMapper.createTable();

    User user = new User();
    user.setUserId(1L);
    user.setUserName("jack");
    user.setPwd("123456");
    userMapper.insert(user);
  }
}
