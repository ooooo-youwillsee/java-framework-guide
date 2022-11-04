package com.ooooo;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

public class ProxyCompositePropertySources extends CompositePropertySources {

  private final MutablePropertySources mutablePropertySources = new MutablePropertySources();

  public ProxyCompositePropertySources(ObjectProvider<List<AbstractSimplePropertySource>> sourcesProvider) {
    super(null);

    sourcesProvider.ifAvailable(sources -> {
      AnnotationAwareOrderComparator.sort(sources);
      for (AbstractSimplePropertySource source : sources) {
        mutablePropertySources.addLast(source);
      }
    });
  }

  public boolean containsProperty(String propertyName) {
    for (String p : propertyNamesFunction.apply(propertyName)) {
      boolean contains = stream().anyMatch(x -> x.containsProperty(p));
      if (contains) {
        return true;
      }
    }

    return false;
  }

  public String getProperty(String propertyName) {
    return getProperty(propertyName, null);
  }

  public String getProperty(String propertyName, String defaultValue) {
    String value = null;
    for (String p : propertyNamesFunction.apply(propertyName)) {
      for (PropertySource<?> ps : mutablePropertySources) {
        value = (String) ps.getProperty(p);
        if (value != null) {
          return value;
        }
      }
    }
    return defaultValue;
  }

  public Map<String, String> getProperties(String... propertyNames) {
    if (propertyNames == null) {
      return null;
    }

    Map<String, String> map = new HashMap<>();
    for (String key : propertyNames) {
      for (String k : propertyNamesFunction.apply(key)) {
        String v = getProperty(k, null);
        if (v != null) {
          map.put(key, v);
        }
      }
    }
    return map;
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


