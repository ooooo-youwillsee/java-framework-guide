package com.ooooo.auth.convert;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

@SuppressWarnings({"unchecked", "rawtypes"})
@Slf4j
@Component
public class ConverterApplyFactoryImpl implements ConverterApplyFactory, InitializingBean {

  @Autowired
  private ObjectProvider<List<Converter<?, ?>>> convertersObjectProvider;

  private final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Converter>> cacheConverterMap = new ConcurrentHashMap<>();

  @Override
  public <T> T convert(Object source, Object target) {
    if (source == null) {
      log.warn("[null] convert to [{}] has error.", target);
      return null;
    }
    if (target == null) {
      log.warn("[{}] convert to [null] has error.", source);
      return null;
    }

    // 识别 Map 转换
    Class<?> sourceType = getInterfaceClazz(source.getClass(), Map.class);
    Class<?> targetType = getInterfaceClazz(target.getClass(), Map.class);

    if (!canConvert(sourceType, targetType)) {
      throw new IllegalArgumentException(String.format("未实现的转换关系[%s]至[%s]", sourceType.getSimpleName(), targetType.getSimpleName()));
    }

    Converter converter = cacheConverterMap.get(sourceType).get(targetType);
    return (T) converter.convert(source, target);
  }

  @Override
  public <T> T convert(Object source, Object target, boolean useDefault) {
    if (source == null) {
      log.warn("[null] convert to [{}] has error.", target);
      return null;
    }
    if (target == null) {
      log.warn("[{}] convert to [null] has error.", source);
      return null;
    }

    // 识别 Map 转换
    Class<?> sourceType = getInterfaceClazz(source.getClass(), Map.class);
    Class<?> targetType = getInterfaceClazz(target.getClass(), Map.class);

    if (canConvert(sourceType, targetType)) {
      return convert(source, target);
    } else if (useDefault) {
      return beanConvert(source, target);
    }
    throw new IllegalArgumentException(String.format("未实现的转换关系[%s]至[%s]", sourceType.getSimpleName(), targetType.getSimpleName()));
  }

  private <T> T beanConvert(Object source, Object targetType) {
    Object obj = targetType;
    BeanUtils.copyProperties(obj, source);
    return (T) obj;
  }

  @Override
  public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
    if (!cacheConverterMap.containsKey(sourceType)) {
      return false;
    }
    return cacheConverterMap.get(sourceType).containsKey(targetType);
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    convertersObjectProvider.ifAvailable(converters -> converters.forEach(converter -> {
      Type[] genericInterfaces = converter.getClass().getGenericInterfaces();
      ResolvableType foundResolvableType = null;
      for (Type genericInterface : genericInterfaces) {
        ResolvableType resolvableType = ResolvableType.forType(genericInterface);
        if (resolvableType.getRawClass() == Converter.class) {
          foundResolvableType = resolvableType;
          break;
        }
      }
      if (foundResolvableType == null) {
        return;
      }
      Class<?> sourceType = foundResolvableType.getGenerics()[0].getRawClass();
      Class<?> targetType = foundResolvableType.getGenerics()[1].getRawClass();
      ConcurrentMap<Class<?>, Converter> map = cacheConverterMap.computeIfAbsent(sourceType, __ -> new ConcurrentHashMap<>());
      Converter prevConverter = map.putIfAbsent(targetType, converter);
      if (prevConverter != null) {
        throw new IllegalArgumentException("exist conflict for sourceType [" + sourceType + "] -> targetType [" + targetType + "]");
      }
    }));
  }

  private Class<?> getInterfaceClazz(Class<?> clazz, Class<?> interfaceClazz) {
    Class<?>[] targetInterfaces = clazz.getInterfaces();
    if (targetInterfaces != null && targetInterfaces.length > 0) {
      for (Class<?> interfaceClass : targetInterfaces) {
        if (interfaceClass.equals(interfaceClazz)) {
          return interfaceClass;
        }
      }
    }
    return clazz;
  }

}
