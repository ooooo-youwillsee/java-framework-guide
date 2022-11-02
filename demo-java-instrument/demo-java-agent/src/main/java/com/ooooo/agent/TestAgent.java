package com.ooooo.agent;

import java.lang.instrument.Instrumentation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * <a href="https://docs.oracle.com/javase/7/docs/api/java/lang/instrument/package-summary.html?is-external=true">document</a>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class TestAgent {

  private static final String MODIFY_CLASS_NAME = "com.ooooo.instrument.service.TestService";


  public static void premain(String agentArgs, Instrumentation inst) {
    log.info("premain");

    transform(MODIFY_CLASS_NAME, inst);
  }


  public static void agentmain(String agentArgs, Instrumentation inst) {
    log.info("agentmain");

    transform(MODIFY_CLASS_NAME, inst);
  }

  @SneakyThrows
  private static void transform(String className, Instrumentation inst) {
    Class<?> clazz = null;
    ClassLoader classLoader = null;

    try {
      clazz = Class.forName(className);
      classLoader = clazz.getClassLoader();
    } catch (Throwable ignored) {
    }

    for (Class<?> c : inst.getAllLoadedClasses()) {
      if (c.getName().equals(className)) {
        clazz = c;
        classLoader = c.getClassLoader();
        break;
      }
    }

    TestClassFileTransformer transformer = new TestClassFileTransformer(className, classLoader);
    inst.addTransformer(transformer, true);
    inst.retransformClasses(clazz);
  }

}
