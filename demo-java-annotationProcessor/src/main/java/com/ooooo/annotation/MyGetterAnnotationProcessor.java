package com.ooooo.annotation;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.Tree;
import com.sun.source.util.SimpleTreeVisitor;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.processing.PrintingProcessor;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor8;
import javax.tools.Diagnostic;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * jvm args
 *
 * <pre>
 * --add-exports jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.main=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.model=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.processing=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED
 * --add-exports jdk.compiler/com.sun.tools.javac.comp=ALL-UNNAMED
 * --add-exports jdk.compiler/sun.nio.ch=ALL-UNNAMED
 *
 * </pre>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see PrintingProcessor
 * @see SimpleElementVisitor8
 * @since 1.0.0
 */
@SupportedAnnotationTypes("com.ooooo.annotation.MyGetter")
public class MyGetterAnnotationProcessor extends AbstractProcessor {

  private ProcessingEnvironment processingEnv;
  private Messager messager;

  private Trees trees;

  private TreeMaker treeMaker;

  private Names names;


  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    super.init(processingEnv);
    this.messager = processingEnv.getMessager();
    messager.printMessage(Diagnostic.Kind.NOTE, " -- note  getter init --");
    ProcessingEnvironment unwrappedProcessingEnv = jbUnwrap(ProcessingEnvironment.class, processingEnv);
    this.trees = Trees.instance(unwrappedProcessingEnv);

    Context context = ((JavacProcessingEnvironment) unwrappedProcessingEnv).getContext();
    this.treeMaker = TreeMaker.instance(context);
    this.names = Names.instance(context);
  }

  private static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
    T unwrapped = null;
    try {
      final Class<?> apiWrappers = wrapper.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
      final Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
      unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
    } catch (Throwable ignored) {
    }
    return unwrapped != null ? unwrapped : wrapper;
  }

  @Override
  public synchronized boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> set = roundEnv.getElementsAnnotatedWith(MyGetter.class);
    set.forEach(element -> {
      Tree jcTree = trees.getTree(element);
      messager.printMessage(Diagnostic.Kind.NOTE, element.getSimpleName() + " has been processed");
      jcTree.accept(new SimpleTreeVisitor<Void, Void>() {
        @Override
        public Void visitClass(ClassTree node, Void p) {
          messager.printMessage(Diagnostic.Kind.NOTE, node.getSimpleName() + " visitClass");
          List<JCTree.JCVariableDecl> list = new ArrayList<>();
          if (node instanceof JCClassDecl) {
            JCClassDecl classDecl = (JCClassDecl) node;
            for (JCTree tree : classDecl.defs) {
              if (tree.getKind().equals(Tree.Kind.VARIABLE)) {
                JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) tree;
                list.add(jcVariableDecl);
              }
            }
            list.forEach(jcVariableDecl -> {
              messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " variable");
              classDecl.defs = classDecl.defs.prepend(makeGetterMethodDecl(jcVariableDecl));
            });
          }

          return defaultAction(node, p);
        }
      }, null);
    });

    return true;
  }

  private JCTree.JCMethodDecl makeGetterMethodDecl(JCTree.JCVariableDecl jcVariableDecl) {
    ListBuffer<JCStatement> statements = new ListBuffer<>();
    statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
    JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
    return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()), jcVariableDecl.vartype,
        com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), com.sun.tools.javac.util.List.nil(), body, null);
  }

  private Name getNewMethodName(Name name) {
    String s = name.toString();
    return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
  }
}
