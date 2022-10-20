package com.ooooo.annotation;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.processing.PrintingProcessor;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;
import com.sun.tools.javac.util.UnsharedNameTable;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor8;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see PrintingProcessor
 * @see SimpleElementVisitor8
 * @since 1.0.0
 */
// todo javac source code
@SupportedAnnotationTypes("com.ooooo.annotation.MyGetter")
public class MyGetterAnnotationProcessor extends AbstractProcessor {

  private Trees mTrees;
  private TreeMaker mTreeMaker;


  @Override
  public synchronized void init(ProcessingEnvironment processingEnv) {
    mTreeMaker = TreeMaker.instance(((JavacProcessingEnvironment) processingEnv).getContext());
    mTrees = Trees.instance(processingEnv);
    super.init(processingEnv);
  }

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(MyGetter.class);

    for (Element element : elements) {
      JCClassDecl classDecl = (JCClassDecl) mTrees.getTree(element);
      addGetterMethod(element, classDecl);
    }

    return true;
  }

  private void addGetterMethod(Element element, JCClassDecl classDecl) {
    Name methodName = getName("get");
    JCBlock methodBody = getMethodBody();
    JCModifiers modifiers = mTreeMaker.Modifiers(Flags.PUBLIC);
    JCExpression returnType = mTreeMaker.TypeIdent(TypeTag.VOID);
    List<JCVariableDecl> parameters = List.nil();
    List<JCTypeParameter> generics = List.nil();
    List<JCExpression> throwz = List.nil();

    JCMethodDecl methodDecl = mTreeMaker.MethodDef(modifiers, methodName, returnType, generics, parameters, throwz, methodBody, null);

    classDecl.defs = classDecl.defs.append(methodDecl);

    // JCExpression importExpression = mTreeMaker.Ident(getName("com"));
    // importExpression = mTreeMaker.Select(importExpression, getName("ooooo"));
    // importExpression = mTreeMaker.Select(importExpression, getName("annotation"));
    // importExpression = mTreeMaker.Select(importExpression, getName("MyGetter"));
    //
    // JCImport importDel = mTreeMaker.Import(importExpression, false);

    // classDecl.defs = classDecl.defs.prepend(importDel);

    System.out.println("classDel:\n" + classDecl);
  }

  private JCBlock getMethodBody() {
    JCExpression printExpression = mTreeMaker.Ident(getName("System"));
    printExpression = mTreeMaker.Select(printExpression, getName("out"));
    printExpression = mTreeMaker.Select(printExpression, getName("println"));

    List<JCExpression> printArgs = List.from(new JCExpression[]{mTreeMaker.Literal("Hello from HelloProcessor!")});

    printExpression = mTreeMaker.Apply(List.nil(), printExpression, printArgs);

    JCStatement call = mTreeMaker.Exec(printExpression);

    List<JCStatement> statements = List.from(new JCStatement[]{call});

    return mTreeMaker.Block(0, statements);
  }

  private Name getName(String name) {
    Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
    Names names = new Names(context);
    return UnsharedNameTable.create(names).fromString(name);
  }

}
