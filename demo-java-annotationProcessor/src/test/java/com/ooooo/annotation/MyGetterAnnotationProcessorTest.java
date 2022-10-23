package com.ooooo.annotation;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import org.junit.jupiter.api.Test;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
class MyGetterAnnotationProcessorTest {

  @Test
  void test() {
    Compilation compilation = javac()
        .withProcessors(new MyGetterAnnotationProcessor())
        .compile(JavaFileObjects.forResource("User.java"));

    assertThat(compilation).succeeded();
    // assertThat(compilation)
    //     .generatedSourceFile("GeneratedHelloWorld")
    //     .hasSourceEquivalentTo(JavaFileObjects.forResource("GeneratedHelloWorld.java"));
  }
}