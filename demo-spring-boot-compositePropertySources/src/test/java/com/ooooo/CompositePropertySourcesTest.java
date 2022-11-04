package com.ooooo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/15 14:12
 * @since 1.0.0
 */
@SpringBootTest(classes = TestPropertySourceConfiguration.class)
public class CompositePropertySourcesTest {

  @Autowired
  private CompositePropertySources compositePropertySources;

  /**
   * 从配置类中获取属性
   *
   * @see TestPropertySourceConfiguration
   */
  @Test
  public void getPropertyForConfigurationClass() {
    String value = compositePropertySources.getProperty("type");
    assertEquals("type-value", value);
  }

  /**
   * 从请求参数中获取
   */
  @Test
  @SneakyThrows
  public void getPropertyForWeb() {
    String key = "key1";
    String value = "1234";

    // 设置请求参数
    MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
    mockHttpServletRequest.setParameter(key, value);
    RequestAttributes requestAttributes = new ServletRequestAttributes(mockHttpServletRequest);
    RequestContextHolder.setRequestAttributes(requestAttributes);

    String property = compositePropertySources.getProperty(key);
    assertEquals(value, property);
  }


}
