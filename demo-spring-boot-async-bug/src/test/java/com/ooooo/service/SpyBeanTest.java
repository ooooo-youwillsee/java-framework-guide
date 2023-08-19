package com.ooooo.service;

import lombok.SneakyThrows;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aop.target.SingletonTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

/**
 * @author ooooo
 * @date 2021/09/11 10:50
 */
@SpringBootTest
class SpyBeanTest {

  @Autowired
  private AdminService adminService;

  @SpyBean
  private UserService userService;

  @SpyBean
  private CglibUserService cglibUserService;

  @Test
  void getUsernameList() {
    doReturn("11111").when(userService).findUsernameById(1L);

    List<String> usernameList = adminService.getUsernameList(Lists.newArrayList(1L, 2L));
    assertEquals("11111", usernameList.get(0));
    assertEquals("username: 2", usernameList.get(1));
  }


  /**
   * @see org.springframework.aop.framework.CglibAopProxy#getProxy(java.lang.ClassLoader)
   * @see AopProxyUtils#completeProxiedInterfaces(AdvisedSupport, boolean)
   */
  @SneakyThrows
  @Test
  void testCglibProxyUserService() {
    if (cglibUserService instanceof Advised advised) {
      CglibUserService cglibProxyUserService = (CglibUserService) advised.getTargetSource().getTarget();
      cglibProxyUserService = spy(cglibProxyUserService);
      advised.setTargetSource(new SingletonTargetSource(cglibProxyUserService));
      doReturn("1").when(cglibProxyUserService).findUsernameById(1L);
    }

    assertEquals("1", cglibUserService.findUsernameById(1L));
    assertEquals("username: 2", cglibUserService.findUsernameById(2L));
  }
}