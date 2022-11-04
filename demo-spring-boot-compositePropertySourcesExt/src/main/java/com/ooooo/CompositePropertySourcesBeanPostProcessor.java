package com.ooooo;

import java.util.function.Function;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
public class CompositePropertySourcesBeanPostProcessor implements BeanPostProcessor {


  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof CompositePropertySources) {
      return new ProxyCompositePropertySources((CompositePropertySources) bean);
    }

    return bean;
  }


  protected static class ProxyCompositePropertySources extends CompositePropertySources {

    private final CompositePropertySources compositePropertySources;

    public ProxyCompositePropertySources(CompositePropertySources compositePropertySources) {
      super(null);
      this.compositePropertySources = compositePropertySources;
    }


    @Override
    public String getProperty(String propertyName, String defaultValue) {
      String[] propertyNames = propertyNamesFunction.apply(propertyName);

      for (String p : propertyNames) {
        String v = compositePropertySources.getProperty(p);
        if (v != null) {
          return v;
        }
      }

      return defaultValue;
    }

    @Override
    public boolean containsProperty(String propertyName) {
      String[] propertyNames = propertyNamesFunction.apply(propertyName);

      for (String p : propertyNames) {
        boolean contains = compositePropertySources.containsProperty(p);
        if (contains) {
          return true;
        }
      }

      return false;
    }
  }


  private static final Function<String, String[]> propertyNamesFunction = (propertyName) -> {
    if (propertyName == null) {
      return new String[0];
    }

    propertyName = propertyName.trim();

    if (propertyName.contains(".")) {
      return new String[]{propertyName, propertyName.substring(propertyName.lastIndexOf(".") + 1)};
    }

    return new String[]{propertyName};
  };
}
