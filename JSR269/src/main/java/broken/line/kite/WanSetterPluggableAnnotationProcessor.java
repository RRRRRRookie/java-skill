//package broken.line.kite;
//
//import com.sun.source.tree.Tree;
//import com.sun.tools.javac.api.JavacTrees;
//import com.sun.tools.javac.code.Flags;
//import com.sun.tools.javac.processing.JavacProcessingEnvironment;
//import com.sun.tools.javac.tree.JCTree;
//import com.sun.tools.javac.tree.TreeMaker;
//import com.sun.tools.javac.tree.TreeTranslator;
//
//import javax.annotation.processing.*;
//import javax.lang.model.element.Element;
//import javax.lang.model.element.TypeElement;
//import javax.tools.Diagnostic;
//import java.util.Set;
//
///**
// * 参考 https://www.cnblogs.com/strongmore/p/13282763.html
// * https://blog.csdn.net/zhangyongjie0533/article/details/89510262
// *
// * @author: wanjia1
// * @date: 2023/4/19
// */
//@SupportedAnnotationTypes("broken.line.kite.WanGetter")
////@SupportedSourceVersion(SourceVersion.RELEASE_17)
//public class WanSetterPluggableAnnotationProcessor extends AbstractProcessor {
//
//
//    private Messager messager;
//    private JavacTrees trees;
//    private TreeMaker treeMaker;
//    private Names names;
//
//    @Override
//    public synchronized void init(ProcessingEnvironment processingEnv) {
//        super.init(processingEnv);
//        this.messager = processingEnv.getMessager();
//        this.trees = JavacTrees.instance(processingEnv);
//        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
//        this.treeMaker = TreeMaker.instance(context);
//        this.names = Names.instance(context);
//    }
//
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        final Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(WanGetter.class);
//        elements.forEach(element -> {
//            JCTree jcTree = trees.getTree(element);
//            jcTree.accept(new TreeTranslator() {
//                @Override
//                public void visitClassDef(JCTree.JCClassDecl tree) {
//                    List<JCTree.JCVariableDecl> jcVariableDeclList = List.nil();
//                    for (JCTree def : tree.defs) {
//                        if (def.getKind().equals(Tree.Kind.VARIABLE)) {
//                            JCTree.JCVariableDecl jcVariableDecl = (JCTree.JCVariableDecl) def;
//                            jcVariableDeclList = jcVariableDeclList.append(jcVariableDecl);
//                        }
//                    }
//                    for (JCTree.JCVariableDecl jcVariableDecl : jcVariableDeclList) {
//                        messager.printMessage(Diagnostic.Kind.NOTE, jcVariableDecl.getName() + " has been processed");
//                        tree.defs = tree.defs.append(addGetterMethod(jcVariableDecl));
//                    }
//                }
//            });
//
//        });
//        return true;
//    }
//
//    private JCTree addGetterMethod(JCTree.JCVariableDecl jcVariableDecl) {
//        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
//        statements.append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), jcVariableDecl.getName())));
//        JCTree.JCBlock body = treeMaker.Block(0, statements.toList());
//        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC), getNewMethodName(jcVariableDecl.getName()), jcVariableDecl.vartype, List.nil(), List.nil(), List.nil(), body, null);
//    }
//
//    private Name getNewMethodName(Name name) {
//        String s = name.toString();
//        return names.fromString("get" + s.substring(0, 1).toUpperCase() + s.substring(1, name.length()));
//    }
//
//
//}
