package com.ooooo.service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StreamUtils;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Service
public class TestService {


  @SneakyThrows
  public String test1(String message) {
    // 查找类
    ClassLoader classLoader = new ModuleAClassLoader();
    Class<?> clazz = classLoader.loadClass("com.ooooo.HelloService");

    // 执行
    Method test1 = ReflectionUtils.findMethod(clazz, "test1", String.class);
    Object result = test1.invoke(null, message);

    return (String) result;
  }

  @SneakyThrows
  public String test2(String message) {
    // 查找类
    ClassLoader classLoader = new ModuleBClassLoader();
    Class<?> clazz = classLoader.loadClass("com.ooooo.HelloService");

    // 执行
    Method test2 = ReflectionUtils.findMethod(clazz, "test2", String.class);
    Object result = test2.invoke(null, message);

    return (String) result;
  }


  private static class ModuleAClassLoader extends ClassLoader {

    public ModuleAClassLoader() {
      super(ModuleAClassLoader.class.getClassLoader());
    }

    @SneakyThrows
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class<?> c = findLoadedClass(name);

      if (c == null) {
        // 当前路径下去找
        if (name.contains("com.ooooo")) {
          String path = name.replace(".", "/") + ".class";
          Enumeration<URL> resources = getResources(path);

          URL targetUrl = null;
          while (resources.hasMoreElements()) {
            targetUrl = resources.nextElement();
            if (targetUrl.toString().contains("module-a")) {
              break;
            }
          }

          // 读取 class 文件
          InputStream in = targetUrl.openStream();
          byte[] bytes = StreamUtils.copyToByteArray(in);
          in.close();

          c = defineClass(name, bytes, 0, bytes.length);
        }
      }

      if (c == null) {
        c = getParent().loadClass(name);
      }

      if (resolve) {
        resolveClass(c);
      }
      return c;
    }
  }


  private static class ModuleBClassLoader extends ClassLoader {

    public ModuleBClassLoader() {
      super(ModuleAClassLoader.class.getClassLoader());
    }

    @SneakyThrows
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      Class<?> c = findLoadedClass(name);

      if (c == null) {
        // 当前路径下去找
        if (name.contains("com.ooooo")) {
          String path = name.replace(".", "/") + ".class";
          Enumeration<URL> resources = getResources(path);

          URL targetUrl = null;
          while (resources.hasMoreElements()) {
            targetUrl = resources.nextElement();
            if (targetUrl.toString().contains("module-b")) {
              break;
            }
          }

          // 读取 class 文件
          InputStream in = targetUrl.openStream();
          byte[] bytes = StreamUtils.copyToByteArray(in);
          in.close();

          c = defineClass(name, bytes, 0, bytes.length);
        }
      }

      if (c == null) {
        c = getParent().loadClass(name);
      }

      if (resolve) {
        resolveClass(c);
      }
      return c;
    }
  }


}
