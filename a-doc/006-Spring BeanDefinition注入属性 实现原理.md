# Spring BeanDefintion注入属性

## 1. 涉及到操作 BeanDefinition 的源码

- 方法 `org.springframework.beans.factory.support.AbstractBeanFactory.getMergedLocalBeanDefinition`： 根据 **parentName(父类的 beanDefinition 名字)** 来合并 BeanDefintion
- 方法 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyMergedBeanDefinitionPostProcessors`: **beanDefinitionPostProcessor** 来处理 beanDefintion
- 方法 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean`: 这个方法中会执行 `InstantiationAwareBeanPostProcessor.postProcessProperties`， 操作 beanDefinition 的 **PropertyValues属性**, 还会调用方法 `applyPropertyValues` 来解析 **PropertyValues属性**
- 方法 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyPropertyValues`： 非常重要，这个方法中执行 `Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);` 来解析属性


# 2. 重要的类 

1. BeanDefinitionValueResolver：解析 PropertyValues 属性

支持解析的类型：

- `RuntimeBeanReference` (重要）:  对应的解析方法 `org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveReference`
  
- `RuntimeBeanNameReference` 
  
- `BeanDefinitionHolder`

- `BeanDefinition`
  
- `DependencyDescriptor` (重要): 注解 `@Autowired` 实现类 `AutowiredAnnotationBeanPostProcessor` 就是用的这个类型，通过调用 `org.springframework.beans.factory.config.AutowireCapableBeanFactory.resolveDependency` 来解析依赖

- `Managed*`(很少用)

- `TypedStringValue` (类型字符串)

- `NullBean`(很少用)

- `其他缺省处理`，用 `Spel表达式` 来处理


## 3. 重要的方法

- org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean
- org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByName
- org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByType 
- org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor.postProcessProperties
- org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyPropertyValues
- org.springframework.beans.factory.support.BeanDefinitionValueResolver.resolveValueIfNecessary
