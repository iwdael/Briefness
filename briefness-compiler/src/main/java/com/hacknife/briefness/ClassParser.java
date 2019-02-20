package com.hacknife.briefness;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.hacknife.briefness.bean.Briefness;
import com.hacknife.briefness.bean.Field;
import com.hacknife.briefness.bean.Immersive;
import com.hacknife.briefness.bean.Method;
import com.hacknife.briefness.util.Logger;

import java.io.File;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class ClassParser {
    public static String BindClick = "BindClick";
    public static String BindView = "BindView";
    public static String BindLayout = "BindLayout";
    public static String Immersive = "Immersive";
    public static String Immersive_statusColor = "statusColor";
    public static String Immersive_navigationColor = "navigationColor";
    public static String Immersive_statusEmbed = "statusEmbed";
    public static String Immersive_navigationEmbed = "navigationEmbed";

    public static void parser(String path, Briefness briefness) {
        try {
            CompilationUnit parse = JavaParser.parse(new File(path));
            VoidVisitorAdapter<Object> adapter = new VoidVisitorAdapter<Object>() {
                @Override
                public void visit(ClassOrInterfaceDeclaration n, Object arg) {
                    super.visit(n, arg);
                    String[] ids = checkAnnatation(n.getAnnotations());
                    if (ids != null && ids.length == 1)
                        briefness.setLayout(ids[0].substring(ids[0].lastIndexOf(".") + 1));
                    briefness.setImmersive(checkImmersive(n.getAnnotations()));
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
            Logger.v(e.getMessage());
        }
    }

    private static String[] checkAnnatation(NodeList<AnnotationExpr> annotationExprs) {
        for (AnnotationExpr annotationExpr : annotationExprs) {
            if (annotationExpr.toString().contains(BindLayout) || annotationExpr.toString().contains(BindView) || annotationExpr.toString().contains(BindClick))
                return subString(annotationExpr.toString().replaceAll("R2", "R")).split(",");
        }
        return null;
    }

    private static Immersive checkImmersive(NodeList<AnnotationExpr> annotationExprs) {
        for (AnnotationExpr annotationExpr : annotationExprs) {
            if (annotationExpr.getNameAsString().equals(Immersive)) {
                String annotation = annotationExpr.toString();
                if (!annotation.contains("(")) return new Immersive();
                String content = annotation.substring(annotation.indexOf("(") + 1, annotation.indexOf(")")).replaceAll(" ", "");
                Immersive im = new Immersive();
                if (content.length() > 4) {
                    String values[] = content.split(",");
                    for (String value : values) {
                        String[] kv = value.split("=");
                        if (kv[0].equals(Immersive_statusColor)) {
                            im.setStatusColor(kv[1]);
                        } else if (kv[0].equals(Immersive_navigationColor)) {
                            im.setNavigationColor(kv[1]);
                        } else if (kv[0].equals(Immersive_statusEmbed)) {
                            im.setStatusEmbed(kv[1]);
                        } else if (kv[0].equals(Immersive_navigationEmbed)) {
                            im.setNavigationEmbed(kv[1]);
                        }
                    }
                }
                return im;
            }
        }
        return null;
    }

    private static String subString(String anntation) {
        if (anntation.contains("{")) {
            int start = anntation.indexOf("{");
            int end = anntation.lastIndexOf("}");
            return anntation.substring(start + 1, end).replaceAll(" ", "");
        } else {
            int start = anntation.indexOf("(");
            int end = anntation.lastIndexOf(")");
            return anntation.substring(start + 1, end).replaceAll(" ", "");
        }
    }

}
