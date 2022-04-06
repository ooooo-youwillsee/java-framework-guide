package com.ooooo.auth.test;

import com.alibaba.fastjson.JSON;
import com.ooooo.auth.authentication.AccessTokenAuthentication;
import com.ooooo.auth.convert.Converter;
import com.ooooo.auth.convert.ConverterApplyFactory;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leizhijie
 * @date 2022/3/31 15:12
 * @since 1.0.0
 */
@Slf4j
@Configuration
@RestController
public class AuthTestController {

  /**
   * <p>@AuthenticationPrincipal 是 spring 原生提供的注解</p>
   * <p>principal 本身就是 map 类型</p>
   *
   * @param userInfo
   * @return
   */
  @PostMapping("/auth/test1")
  public Result<?> test(@AuthenticationPrincipal Map<String, Object> userInfo) {
    log.info("/auth/test1");
    log.info("client_id: {}", userInfo.get("client_id"));
    log.info("client_name: {}", userInfo.get("client_name"));
    return new Result<>(userInfo);
  }

  /**
   * <p>@AuthenticationPrincipal 是 spring 原生提供的注解</p>
   * <p>有 AccessTokenAuthenticationConvertTestLoginUser 这个装换类, 所以能直接传入 user 对象</p>
   *
   * @param userInfo
   * @return
   * @see AccessTokenAuthenticationConvertTestLoginUser
   */
  @PostMapping("/auth/test2")
  public Result<?> test(@AuthenticationPrincipal TestLoginUser userInfo) {
    log.info("/auth/test2");
    log.info("client_id: {}", userInfo.getClient_id());
    log.info("client_name: {}", userInfo.getClient_name());
    return new Result<>(userInfo);
  }


  @Data
  public static class TestLoginUser {

    private String client_id;

    private String client_name;
  }


  /**
   * @see AccessTokenAuthentication
   * @see ConverterApplyFactory
   * @see TestLoginUser
   */
  @Component
  public static class AccessTokenAuthenticationConvertTestLoginUser implements Converter<AccessTokenAuthentication, TestLoginUser> {

    @Override
    public TestLoginUser convert(AccessTokenAuthentication source, TestLoginUser target) {
      Object principal = source.getPrincipal();
      return JSON.parseObject(JSON.toJSONString(principal), target.getClass());
    }
  }


  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class Result<T> {

    private T data;

    private String code = "0";

    private String message = "OK";

    public Result(T data) {
      this.data = data;
    }

    public static <T> Result<T> success(T data) {
      return new Result<>(data);
    }

  }
}
