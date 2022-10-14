package com.ooooo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ooooo.caffeine.UserService;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class UserServiceTest {

  private MethodType methodType;
  private Lookup lookup;
  private MethodHandle methodHandle;

  @SneakyThrows
  @BeforeEach
  public void beforeEach() {
    methodType = MethodType.methodType(String.class, String.class);
    lookup = MethodHandles.lookup();
    methodHandle = lookup.findVirtual(UserService.class, "getUsername", methodType);
  }

  @SneakyThrows
  @Test
  public void invokeWithArguments() {
    UserService userService = new UserService();
    Object obj = methodHandle.bindTo(userService).invokeWithArguments("1");
    assertEquals("username1", obj);

  }

  @SneakyThrows
  @Test
  public void invoke() {
    UserService userService = new UserService();
    Object obj = methodHandle.invoke(userService, "1");
    assertEquals("username1", obj);
  }

  /**
   * 要求类型全匹配, 包括返回值类型
   */
  @SneakyThrows
  @Test
  public void invokeExact() {
    UserService userService = new UserService();
    String s = (String) methodHandle.invokeExact(userService, "1");
    assertEquals("username1", s);
  }

}
