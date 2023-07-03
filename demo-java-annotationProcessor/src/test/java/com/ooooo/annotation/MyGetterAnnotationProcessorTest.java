package com.ooooo.annotation;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import lombok.Cleanup;
import org.springframework.util.FileCopyUtils;

import javax.tools.JavaFileObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static com.google.testing.compile.Compiler.javac;

/**
 * jvm args
 * <pre>
 *   --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
 *   --add-exports jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED
 *   --add-exports jdk.compiler/sun.nio.ch=ALL-UNNAMED
 * </pre>
 *
 * <a href="https://chermehdi.com/posts/compiler-testing-tutorial/">compiler-testing</a>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
class MyGetterAnnotationProcessorTest {

  private static final String path = "/Users/ooooo/Code/Self/java-framework-guide/demo-java-annotationProcessor/src/test/java/";

  public static void main(String[] args) throws IOException {
    Compilation compilation = javac()
        .withProcessors(new MyGetterAnnotationProcessor())
        .compile(JavaFileObjects.forResource("User.java"));

    assertThat(compilation).succeeded();

    for (JavaFileObject file : compilation.generatedFiles()) {
      File newFile = new File(path + file.getName());
      if (!newFile.getParentFile().exists()) {
        newFile.getParentFile().mkdirs();
      }
      @Cleanup InputStream in = file.openInputStream();
      @Cleanup FileOutputStream out = new FileOutputStream(newFile);
      FileCopyUtils.copy(in, out);
    }
  }
}