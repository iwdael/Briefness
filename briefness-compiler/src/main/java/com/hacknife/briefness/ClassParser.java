package com.hacknife.briefness;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.hacknife.briefness.bean.Briefness;
import com.hacknife.briefness.bean.Field;
import com.hacknife.briefness.bean.Method;

import java.io.File;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class ClassParser {
    public static String BindClick = "BindClick";
    public static String BindView = "BindView";
    public static String BindLayout = "BindLayout";

    public static void parser(String path, Briefness briefness) {
        try {
            CompilationUnit parse = JavaParser.parse(new File(path));
            VoidVisitorAdapter<Object> adapter = new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                    super.visit(n, arg);
                    String[] ids = checkAnnatation(n.getAnnotations());
                    briefness.setLayout(ids[0].substring(ids[0].lastIndexOf(".") + 1));
                }

                @Override
                public void visit(FieldDeclaration n, Object arg) {
                    super.visit(n, arg);
                    String[] ids = checkAnnatation(n.getAnnotations());
                    if (ids != null)
                        briefness.addField(new Field(n.getCommonType().toString(), n.getVariable(0).toString(), ids));
                }

                @Override
                public void visit(MethodDeclaration n, Object arg) {
                    super.visit(n, arg);
                    String[] ids = checkAnnatation(n.getAnnotations());
                    if (ids != null)
                        briefness.addMethod(new Method(n.getNameAsString(), ids));
                }
            };
            adapter.visit(parse, null);
        } catch (Exception e) {
        }
    }

    private static String[] checkAnnatation(NodeList<AnnotationExpr> annotationExprs) {
        for (AnnotationExpr annotationExpr : annotationExprs) {
            if (annotationExpr.toString().contains(BindLayout) || annotationExpr.toString().contains(BindView) || annotationExpr.toString().contains(BindClick))
                return subString(annotationExpr.toString()).split(",");
        }
        return null;
    }

    private static String subString(String anntation) {
        if (anntation.contains("{")) {
            int start = anntation.indexOf("{");
            int end = anntation.lastIndexOf("}");
            return anntation.substring(start + 1, end).replaceAll(" ", "").replaceAll("R2","R");
        } else {
            int start = anntation.indexOf("(");
            int end = anntation.lastIndexOf(")");
            return anntation.substring(start + 1, end).replaceAll(" ", "").replaceAll("R2","R");
        }

    }


//    public static void main(String[] argv) {
//        Briefness briefness = new Briefness("");
//        parser("C:\\Users\\Hacknife\\Desktop\\briefness\\example\\src\\main\\java\\com\\hacknife\\demo\\MainActivity.java", briefness);
//        System.out.print(briefness.toString());
//    }
}
