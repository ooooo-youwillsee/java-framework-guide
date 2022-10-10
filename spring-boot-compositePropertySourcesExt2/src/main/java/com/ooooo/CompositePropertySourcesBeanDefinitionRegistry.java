package com.ooooo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
public class CompositePropertySourcesBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

  public static final String COMPOSITE_PROPERTY_SOURCES_BEAN_NAME = "compositePropertySources";

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    if (registry.containsBeanDefinition(COMPOSITE_PROPERTY_SOURCES_BEAN_NAME)) {
      registry.removeBeanDefinition(COMPOSITE_PROPERTY_SOURCES_BEAN_NAME);

      RootBeanDefinition definition = new RootBeanDefinition(ProxyCompositePropertySources.class);
      definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_CONSTRUCTOR);

      registry.registerBeanDefinition(COMPOSITE_PROPERTY_SOURCES_BEAN_NAME, definition);
    }
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

  }


}
