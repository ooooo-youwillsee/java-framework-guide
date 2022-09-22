package com.ooooo.impl;

import com.ooooo.AbstractSimplePropertySource;
import org.springframework.core.annotation.Order;

import static com.ooooo.EnvironmentAutoConfiguration.ENV_PREFIX;

/**
 * <p>从环境变量读取 例如：_ENV_XXX</p>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 */
@Order(2)
public class EnvironmentPropertySource extends AbstractSimplePropertySource {


  public EnvironmentPropertySource() {
    super("environment");
  }

  @Override
  public Object getProperty(String name) {
    String env_property_key = ENV_PREFIX + name.replace(".", "_");
    return System.getenv(env_property_key.toUpperCase());
  }
}
