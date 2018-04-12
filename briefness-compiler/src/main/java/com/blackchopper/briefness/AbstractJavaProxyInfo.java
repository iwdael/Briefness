package com.blackchopper.briefness;


import com.blackchopper.briefness.databinding.JavaLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import com.blackchopper.briefness.databinding.XmlViewInfo;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public abstract class AbstractJavaProxyInfo {
    public static final String PROXY = "Briefnessor";
    /**
     * MainActivity
     */
    protected String className;
    protected StringBuilder importBuilder = new StringBuilder();

    protected String packageName;
    /**
     * MainActivityBriefnessor
     */
    protected String proxyClassName;
    protected TypeElement typeElement;

    public Map<int[], Element> bindView = new LinkedHashMap<>();
    public List<JavaLayout> bindLayout = new ArrayList<>();

    public Map<int[], Element> bindClick = new LinkedHashMap<>();


    public AbstractJavaProxyInfo(Elements elementUtils, TypeElement classElement) {
        this.typeElement = classElement;
        PackageElement packageElement = elementUtils.getPackageOf(classElement);
        String packageName = packageElement.getQualifiedName().toString();
        className = ClassValidator.getClassName(classElement, packageName);
        this.packageName = packageName;
        this.proxyClassName = className + PROXY;
    }

    public String getProxyClassFullName() {
        return packageName + "." + proxyClassName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }


    public String generateJavaCode() {
        StringBuilder builder = new StringBuilder();

        importBuilder.append("// Generated code. Do not modify! \n");
        importBuilder.append("// ").append(typeElement.toString()).append(" \n");
        importBuilder.append("package ").append(packageName).append(";\n");

        importBuilder.append("import ").append(ClassUtil.findPackageName()).append(".R;\n");
        importBuilder.append("import com.blackchopper.briefness.*;\n");
        importBuilder.append("import android.view.*;\n");
        importBuilder.append("import android.widget.*;\n");
        importBuilder.append("import android.app.Activity;\n");
        importBuilder.append("import java.util.ArrayList;\n\n");

        importBuilder.append("import " + typeElement.getQualifiedName()).append(";\n\n");

        builder.append("public class ").append(proxyClassName).append(" implements " + PROXY).append("<").append(className).append(">");
        builder.append("{\n");

        generateComplierCode(builder);
        builder.append("\n").append("}\n");

        return importBuilder.append(builder.toString()).toString();
    }


    private void generateComplierCode(StringBuilder builder) {

        generateFieldCode(builder);
        if (ClassUtil.instanceOfActivity(typeElement.getQualifiedName().toString())) {
            //activity
            generateBindActivityCode(builder);
        } else {
            //fragment or other
            generateBindOtherCode(builder);
        }
        generateBindDataCode(builder);

    }


    private void generateBindOtherCode(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public void bind(final " + className + " host, Object source) {\n" +
                " View view=(View)source;\n");
        generateBindFieldCode(builder, false);
        generateMethodCode(builder, false);
        generateXmlClickCode(builder);
        builder.append("    }");
    }

    private void generateBindActivityCode(StringBuilder builder) {
        builder.append("    @Override\n" +
                "    public void bind(final " + className + " host, Object source) {\n");
        generateLayoutCode(builder);
        generateBindFieldCode(builder, true);
        generateMethodCode(builder, true);
        generateXmlClickCode(builder);
        builder.append("    }");
    }

    protected abstract void generateLayoutCode(StringBuilder builder);

    protected abstract void generateBindFieldCode(StringBuilder builder, boolean b);

    protected abstract void generateBindDataCode(StringBuilder builder);

    protected abstract void generateFieldCode(StringBuilder builder);


    private void generateXmlClickCode(StringBuilder builder) {
        if (bindLayout.size() > 0) {
            XmlProxyInfo proxyInfo = new XmlProxyInfo(ClassUtil.findLayoutById(typeElement.getQualifiedName().toString()));
            List<XmlViewInfo> infos = proxyInfo.getViewInfos();
            for (XmlViewInfo info : infos) {
                if (info.click != null) {
                    builder.append(info.ID).append(".setOnClickListener(new View.OnClickListener() {\n" +
                            "                @Override\n" +
                            "                public void onClick(View v) {\n" +
                            "                    host." + info.click + "\n" +
                            "                }\n" +
                            "            });");
                }
                if (info.longClick != null) {
                    builder.append(info.ID).append(".setOnLongClickListener(new View.OnLongClickListener() {\n" +
                            "            @Override\n" +
                            "            public boolean onLongClick(View v) {\n" +
                            "            host." + info.longClick + "\n" +
                            "                return false;\n" +
                            "            }\n" +
                            "        });");
                }
                if (info.touch != null) {
                    builder.append(info.ID).append(".setOnTouchListener(new View.OnTouchListener() {\n" +
                            "            @Override\n" +
                            "            public boolean onTouch(View v, MotionEvent event) {\n" +
                            "            host." + info.touch + "\n" +
                            "                return false;\n" +
                            "            }\n" +
                            "        });");
                }
            }
        }
    }


    private void generateMethodCode(StringBuilder builder, boolean isActivity) {
        for (Map.Entry<int[], Element> entry : bindClick.entrySet()) {
            for (int id : entry.getKey()) {
                if (isActivity)
                    builder.append("host.findViewById(").append(id + ").setOnClickListener(new View.OnClickListener() {\n");
                else
                    builder.append("view.findViewById(").append(id + ").setOnClickListener(new View.OnClickListener() {\n");
                builder.append("@Override\n");
                builder.append("public void onClick(View view) {\n");
                builder.append("host.").append(entry.getValue().getSimpleName()).append("(view);\n");
                builder.append("}\n");
                builder.append(" });\n");
            }
        }
    }


}
