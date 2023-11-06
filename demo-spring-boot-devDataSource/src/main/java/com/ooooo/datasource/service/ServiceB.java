package com.ooooo.datasource.service;

import com.ooooo.datasource.dao.entity.User;
import com.ooooo.datasource.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Service
public class ServiceB {

  @Autowired
  private UserMapper userMapper;

  @Transactional(propagation = Propagation.REQUIRED)
  public void testB() {
    userMapper.insert(new User(333L, "serviceB", "xxx"));
    throw new IllegalArgumentException("xxx");
  }
}
