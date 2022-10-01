package com.ooooo.agent;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class TestClassFileTransformer implements ClassFileTransformer {

  private final String targetClassName;
  private final ClassLoader targetClassLoader;

  public TestClassFileTransformer(String targetClassName, ClassLoader targetClassLoader) {
    this.targetClassName = targetClassName;
    this.targetClassLoader = targetClassLoader;
  }

  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer)
      throws IllegalClassFormatException {
    log.info("className: {}, targetClassName: {}, loader: {}, targetClassLoader: {}", className, targetClassName, loader, targetClassLoader);

    String finalTargetClassName = targetClassName.replace(".", "/");

    if (!finalTargetClassName.equals(className)) {
      return classfileBuffer;
    }

    if (targetClassLoader == null || targetClassLoader.equals(loader)) {
      log.info("modify className: {}", targetClassName);

      ClassPool classPool = ClassPool.getDefault();
      classPool.appendClassPath(new LoaderClassPath(targetClassLoader));

      try {
        CtClass c = classPool.get(targetClassName);

        // test1
        CtMethod test1Method = c.getDeclaredMethod("test1");
        test1Method.setBody("return \"modify test1\";");

        // test2
        CtMethod test2Method = c.getDeclaredMethod("test2");
        test2Method.setBody("return \"modify test2\";");

        return c.toBytecode();
      } catch (Throwable e) {
        log.error("transform error.", e);
      }
    }

    return classfileBuffer;
  }
}
