package com.aliletter.briefness;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/9/15.
 */

public class ProxyInfo {
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    public Map<int[], Element> briefnessVariable = new LinkedHashMap<>();
    public static final String PROXY = "Briefnessor";
    public Map<int[], Element> briefnessMethod = new LinkedHashMap<>();

    public ProxyInfo(Elements elementUtils, TypeElement classElement) {
        this.typeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        String className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + "$$" + PROXY;
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }


    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();
        builder.append("// Generated code. Do not modify!\n");
        builder.append("package ").append(packageName).append(";\n");
        builder.append("import com.aliletter.briefness.*;\n");
        builder.append("import android.view.View;\n");
        builder.append("import android.widget.*;\n");
        builder.append("import java.util.ArrayList;\n\n");

        builder.append("public class ").append(proxyClassName).append(" implements " + PROXY + "<" + typeElement.getQualifiedName() + ">");
        builder.append("{\n");

        generateComplierCode(builder);
        builder.append("\n").append("}\n");
        return builder.toString();
    }

    private void generateComplierCode(StringBuilder builder) {

        builder.append("@Override\n ");
        builder.append("public void bind(final " + typeElement.getQualifiedName() + " host, Object source ) {\n");
        builder.append("if (source instanceof android.app.Activity) {\n");
        for (int[] ids : briefnessVariable.keySet()) {
            switch (ids.length) {
                case 0:
                    break;
                case 1:
                    generateVariableCode(ids, builder, true);
                    generateLayoutCode(ids, builder, true);
                    break;
                default:
                    generateVariableCodes(ids, builder, true);
                    break;
            }
        }
        generateMethodCode(builder, true);
        builder.append("\n}else{\n");
        for (int[] ids : briefnessVariable.keySet()) {
            switch (ids.length) {
                case 0:
                    break;
                case 1:
                    generateVariableCode(ids, builder, false);
                    generateLayoutCode(ids, builder, false);
                    break;
                default:
                    generateVariableCodes(ids, builder, false);
                    break;
            }
        }
        generateMethodCode(builder, false);
        builder.append("  }\n");


        builder.append("  }\n");
    }

    private void generateMethodCode(StringBuilder builder, boolean isActivity) {
        if (isActivity) {
            for (Map.Entry<int[], Element> entry : briefnessMethod.entrySet()) {
                for (int id : entry.getKey()) {
                    builder.append("((android.app.Activity)source).findViewById(").append(id + ").setOnClickListener(new View.OnClickListener() {\n");
                    builder.append("@Override\n");
                    builder.append("public void onClick(View view) {\n");
                    builder.append("host.").append(entry.getValue().getSimpleName()).append("(view);\n");
                    builder.append("}\n");
                    builder.append(" });\n");
                }
            }
        } else {
            for (Map.Entry<int[], Element> entry : briefnessMethod.entrySet()) {
                for (int id : entry.getKey()) {
                    builder.append("((android.view.View)source).findViewById(").append(id + ").setOnClickListener(new View.OnClickListener() {\n");
                    builder.append("@Override\n");
                    builder.append("public void onClick(View view) {\n");
                    builder.append("host.").append(entry.getValue().getSimpleName()).append("(view);\n");
                    builder.append("}\n");
                    builder.append(" });\n");
                }
            }
        }
    }

    private void generateVariableCodes(int[] ids, StringBuilder builder, boolean isActivity) {
        if (isActivity) {
            try {
                VariableElement element = (VariableElement) briefnessVariable.get(ids);
                TypeElement typeElement = (TypeElement) element.getEnclosingElement();
                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("host." + name).append(" = ");
                builder.append("new " + type + "{\n");
                for (int id : ids) {
                    builder.append("(" + type.replace("[", "").replace("]", "") + ")(((android.app.Activity)source).findViewById( " + id + ")),\n");
                }
                builder.delete(builder.length() - 2, builder.length()).append("\n");
                builder.append("};\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {


            try {
                VariableElement element = (VariableElement) briefnessVariable.get(ids);

                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("host." + name).append(" = ");
                builder.append("new " + type + "{\n");
                for (int id : ids) {
                    builder.append("(" + type.replace("[", "").replace("]", "") + ")(((android.view.View)source).findViewById( " + id + ")),\n");
                }
                builder.delete(builder.length() - 2, builder.length()).append("\n");
                builder.append("};\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void generateLayoutCode(int[] ids, StringBuilder builder, boolean isActivity) {
        try {
            if (briefnessVariable.get(ids) == null)
                builder.append("host.setContentView(").append(ids[0]).append(");\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateVariableCode(int[] ids, StringBuilder builder, boolean isActivity) {
        if (isActivity) {
            try {
                if (briefnessVariable.get(ids) == null) return;
                VariableElement element = (VariableElement) briefnessVariable.get(ids);
                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("host." + name).append(" = ");
                builder.append("(" + type + ")(((android.app.Activity)source).findViewById( " + ids[0] + "));\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (briefnessVariable.get(ids) == null) return;
                VariableElement element = (VariableElement) briefnessVariable.get(ids);
                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("host." + name).append(" = ");
                builder.append("(" + type + ")(((android.view.View)source).findViewById( " + ids[0] + "));\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
