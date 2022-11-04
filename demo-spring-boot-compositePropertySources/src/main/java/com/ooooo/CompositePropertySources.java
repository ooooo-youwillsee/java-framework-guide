package com.ooooo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

/**
 * 统一的配置管理
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @date 2020/11/15 10:47
 * @see PropertySources
 * @see AbstractSimplePropertySource
 */
@Slf4j
@Order
public class CompositePropertySources implements PropertySources {

  private final MutablePropertySources mutablePropertySources = new MutablePropertySources();

  public CompositePropertySources(List<AbstractSimplePropertySource> sources) {
    if (sources == null) {
      return;
    }

    AnnotationAwareOrderComparator.sort(sources);
    for (AbstractSimplePropertySource source : sources) {
      mutablePropertySources.addLast(source);
    }
  }

  public boolean containsProperty(String propertyName) {
    return stream().anyMatch(p -> p.containsProperty(propertyName));
  }

  public String getProperty(String propertyName) {
    return getProperty(propertyName, null);
  }

  public String getProperty(String propertyName, String defaultValue) {
    String value = null;
    for (PropertySource<?> ps : mutablePropertySources) {
      value = (String) ps.getProperty(propertyName);
      if (value != null) {
        return value;
      }
    }
    return defaultValue;
  }

  public Map<String, String> getProperties(String... propertyNames) {
    if (propertyNames != null) {
      Map<String, String> map = new HashMap<>();
      for (String key : propertyNames) {
        map.put(key, getProperty(key, null));
      }
      return map;
    }
    return null;
  }

  // =============== mutablePropertySources relevant method ==================================

  @Override
  public boolean contains(String name) {
    return mutablePropertySources.contains(name);
  }

  @Override
  public PropertySource<?> get(String name) {
    return mutablePropertySources.get(name);
  }

  @Override
  public Iterator<PropertySource<?>> iterator() {
    return mutablePropertySources.iterator();
  }

  public Stream<PropertySource<?>> stream() {
    return mutablePropertySources.stream();
  }
}
