package com.ooooo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.util.ReflectionTestUtils.getField;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.MutablePropertySources;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2021/11/15 14:12
 * @since 1.0.0
 */
@SpringBootTest(classes = TestPropertySourceConfiguration.class)
public class ProxyCompositePropertySourcesTest {

  @Autowired
  private CompositePropertySources compositePropertySources;


  @Test
  public void test() {
    String value = compositePropertySources.getProperty("tom");
    assertEquals("123", value);

    value = compositePropertySources.getProperty("businType.tom");
    assertEquals("456", value);

    // remove test2, the propertyName 'businType.tom' is removed
    removePropertySource("test2");

    value = compositePropertySources.getProperty("businType.tom");
    assertEquals("123", value);
  }

  private void removePropertySource(String name) {
    MutablePropertySources mutablePropertySources = (MutablePropertySources) getField(compositePropertySources, compositePropertySources.getClass(), "mutablePropertySources");
    mutablePropertySources.remove(name);
  }

}
