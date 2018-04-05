package  briefness;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;

import briefness.databinding.JavaLayout;
import briefness.databinding.XmlBind;
import briefness.databinding.XmlViewInfo;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public class JavaProxyInfo {
    public static final String PROXY = "Briefnessor";
    StringBuilder importBuilder = new StringBuilder();
    private String packageName;
    private String proxyClassName;
    private TypeElement typeElement;

    public Map<int[], Element> bindView = new LinkedHashMap<>();
    public List<JavaLayout> bindLayout = new ArrayList<>();

    public Map<int[], Element> bindClick = new LinkedHashMap<>();


    public JavaProxyInfo(Elements elementUtils, TypeElement classElement) {
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

        importBuilder.append("// Generated code. Do not modify! \n");

        XmlProxyInfo proxyInfo1 = new XmlProxyInfo(bindLayout.get(0).layout);
        importBuilder.append("//").append(proxyInfo1.module).append("\n");
        importBuilder.append("// ").append(proxyInfo1.xml).append("\n");
        importBuilder.append("//  ").append(proxyInfo1.getBinds().toString());

        importBuilder.append("package ").append(packageName).append(";\n");
        importBuilder.append("import briefness.*;\n");
        importBuilder.append("import android.view.*;\n");
        importBuilder.append("import android.widget.*;\n");
        importBuilder.append("import android.app.Activity;\n");
        importBuilder.append("import java.util.ArrayList;\n\n");
        importBuilder.append("import " + typeElement.getQualifiedName()).append(";\n\n");

        builder.append("public class ").append(proxyClassName).append(" implements " + PROXY);
        builder.append("{\n");

        generateComplierCode(builder);
        builder.append("\n").append("}\n");

        return importBuilder.append(builder.toString()).toString();
    }

    private void generateFieldCode(StringBuilder builder) {
        if (bindLayout.size() > 0 & bindLayout.get(0).layout != null) {
            XmlProxyInfo proxyInfo = new XmlProxyInfo(bindLayout.get(0).layout);
            List<XmlViewInfo> infos = proxyInfo.getViewInfos();
            for (int i = 0; i < infos.size(); i++) {
                builder.append(infos.get(i).view).append(" ").append(infos.get(i).ID).append(";\n");
            }
        }
    }

    private void generateComplierCode(StringBuilder builder) {

        generateFieldCode(builder);
        builder.append("@Override\n ");
        builder.append("public void bind(final " + "Object" + " host, Object source ) {\n");
        builder.append("if ((source != null)&&(source instanceof Activity)) {\n");
        generateLayoutCode(builder, true);
        if (bindLayout.size() > 0 & bindLayout.get(0).layout != null) {
            XmlProxyInfo proxyInfo = new XmlProxyInfo(bindLayout.get(0).layout);
            List<XmlViewInfo> infos = proxyInfo.getViewInfos();
            for (int i = 0; i < infos.size(); i++) {
                builder.append(infos.get(i).ID).append("=");
                builder.append("(" + infos.get(i).view + ")(((Activity)source).findViewById( R.id." + infos.get(i).ID + "));\n");
            }
        }
        for (int[] ids : bindView.keySet()) {
            switch (ids.length) {
                case 1:
                    generateVariableCode(ids, builder, true);
                    break;
                default:
                    generateVariableCodes(ids, builder, true);
                    break;
            }
        }
        generateMethodCode(builder, true);
        generateXmlClickCode(builder, true);
        builder.append("\n}else if((source != null)&&(source instanceof View )){\n");
        generateLayoutCode(builder, false);
        if (bindLayout.size() > 0 & bindLayout.get(0).layout != null) {
            XmlProxyInfo proxyInfo = new XmlProxyInfo(bindLayout.get(0).layout);
            List<XmlViewInfo> infos = proxyInfo.getViewInfos();
            for (int i = 0; i < infos.size(); i++) {
                builder.append(infos.get(i).ID).append("=");
                builder.append("(" + infos.get(i).view + ")(((View)source).findViewById( R.id." + infos.get(i).ID + "));\n");
            }
        }
        for (int[] ids : bindView.keySet()) {
            switch (ids.length) {
                case 1:
                    generateVariableCode(ids, builder, false);
                    break;
                default:
                    generateVariableCodes(ids, builder, false);
                    break;
            }
        }
        generateMethodCode(builder, false);
        generateXmlClickCode(builder, true);
        builder.append("  \n}");
        generateXMLBindCode(builder);
        builder.append("  }\n");
    }

    private void generateXmlClickCode(StringBuilder builder, boolean isActivity) {
        String clazzName = typeElement.getQualifiedName().toString();
        String name = clazzName.substring(clazzName.lastIndexOf(".") + 1);

        if (bindLayout.size() > 0 & bindLayout.get(0).layout != null) {
            XmlProxyInfo proxyInfo = new XmlProxyInfo(bindLayout.get(0).layout);
            List<XmlViewInfo> infos = proxyInfo.getViewInfos();
            for (XmlViewInfo info : infos) {
                if (info.click != null) {
                    builder.append(info.ID).append(".setOnClickListener(new View.OnClickListener() {\n" +
                            "                @Override\n" +
                            "                public void onClick(View v) {\n" +
                            "                    ((" + name + ")host)." + info.click + "\n" +
                            "                }\n" +
                            "            });");
                }
                if (info.longClick != null) {
                    builder.append(info.ID).append(".setOnLongClickListener(new View.OnLongClickListener() {\n" +
                            "            @Override\n" +
                            "            public boolean onLongClick(View v) {\n" +
                            "            ((" + name + ")host)." + info.longClick + "\n" +
                            "                return false;\n" +
                            "            }\n" +
                            "        });");
                }
                if (info.touch != null) {
                    builder.append(info.ID).append(".setOnTouchListener(new View.OnTouchListener() {\n" +
                            "            @Override\n" +
                            "            public boolean onTouch(View v, MotionEvent event) {\n" +
                            "            ((" + name + ")host)." + info.touch + "\n" +
                            "                return false;\n" +
                            "            }\n" +
                            "        });");
                }
            }
        }
    }

    private void generateXMLBindCode(StringBuilder builder) {
        if (bindLayout.size() > 0 & bindLayout.get(0).layout != null) {
            XmlProxyInfo proxyInfo = new XmlProxyInfo(bindLayout.get(0).layout);
            List<XmlBind> binds = proxyInfo.getBinds();
            for (XmlBind bind : binds) {
                importBuilder.append("import " + bind.clazz).append(";\n");
                builder.append("else if((host != null)&&(host instanceof " + bind.clazz.substring(bind.clazz.lastIndexOf(".") + 1) + " )){\n");
                builder.append(bind.clazz.substring(bind.clazz.lastIndexOf(".") + 1) + " ").append(bind.name).append(" =").append("(")
                        .append(bind.clazz.substring(bind.clazz.lastIndexOf(".") + 1) + ")host;\n");
                for (XmlViewInfo info : bind.list) {
                    if (info.bind == null) continue;
                    if (info.bind.endsWith(";")) {
                        String[] method = info.bind.split(";");
                        for (String s : method) {
                            builder.append(info.ID).append(".").append(s).append(";\n");
                        }
                    } else {
                        builder.append("ViewInjector.inject(").append(info.ID).append(",").append(info.bind).append(");\n");
                    }
                }
                builder.append("}");
            }
        }
    }

    private void generateMethodCode(StringBuilder builder, boolean isActivity) {
        String clazzName = null;
        if (!bindClick.isEmpty()) clazzName = typeElement.getQualifiedName().toString();
        if (isActivity) {
            for (Map.Entry<int[], Element> entry : bindClick.entrySet()) {
                for (int id : entry.getKey()) {
                    builder.append("((Activity)source).findViewById(").append(id + ").setOnClickListener(new View.OnClickListener() {\n");
                    builder.append("@Override\n");
                    builder.append("public void onClick(View view) {\n");
                    builder.append("((" + clazzName.substring(clazzName.lastIndexOf(".") + 1) + ")host).").append(entry.getValue().getSimpleName()).append("(view);\n");
                    builder.append("}\n");
                    builder.append(" });\n");
                }
            }
        } else {
            for (Map.Entry<int[], Element> entry : bindClick.entrySet()) {
                for (int id : entry.getKey()) {
                    builder.append("((View)source).findViewById(").append(id + ").setOnClickListener(new View.OnClickListener() {\n");
                    builder.append("@Override\n");
                    builder.append("public void onClick(View view) {\n");
                    builder.append("((" + clazzName.substring(clazzName.lastIndexOf(".") + 1) + ")host).").append(entry.getValue().getSimpleName()).append("(view);\n");
                    builder.append("}\n");
                    builder.append(" });\n");
                }
            }
        }
    }

    private void generateVariableCodes(int[] ids, StringBuilder builder, boolean isActivity) {
        if (isActivity) {
            try {
                VariableElement element = (VariableElement) bindView.get(ids);
                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("((" + typeElement.getQualifiedName() + ")host)." + name).append(" = ");
                builder.append("new " + type + "{\n");
                for (int id : ids) {
                    builder.append("(" + type.replace("[", "").replace("]", "") + ")(((Activity)source).findViewById( " + id + ")),\n");
                }
                builder.delete(builder.length() - 2, builder.length()).append("\n");
                builder.append("};\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                VariableElement element = (VariableElement) bindView.get(ids);

                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("((" + typeElement.getQualifiedName() + ")host)." + name).append(" = ");
                builder.append("new " + type + "{\n");
                for (int id : ids) {
                    builder.append("(" + type.replace("[", "").replace("]", "") + ")(((View)source).findViewById( " + id + ")),\n");
                }
                builder.delete(builder.length() - 2, builder.length()).append("\n");
                builder.append("};\n");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void generateLayoutCode(StringBuilder builder, boolean isActivity) {
        String clazzName = typeElement.getQualifiedName().toString();
        if (bindLayout.size() > 0)
            builder.append("((" + clazzName.substring(clazzName.lastIndexOf(".") + 1) + ")host).setContentView(").append(bindLayout.get(0).id).append(");\n");

    }

    private void generateVariableCode(int[] ids, StringBuilder builder, boolean isActivity) {
        String clazzName = typeElement.getQualifiedName().toString();
        if (isActivity) {
            try {
                if (bindView.get(ids) == null) return;
                VariableElement element = (VariableElement) bindView.get(ids);
                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("((" + clazzName.substring(clazzName.lastIndexOf("0") + 1) + ")host)." + name).append(" = ");
                builder.append("(" + type + ")(((Activity)source).findViewById( " + ids[0] + "));\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                if (bindView.get(ids) == null) return;
                VariableElement element = (VariableElement) bindView.get(ids);
                String name = element.getSimpleName().toString();
                String type = element.asType().toString();
                builder.append("((" + clazzName.substring(clazzName.lastIndexOf("0") + 1) + ")host)." + name).append(" = ");
                builder.append("(" + type + ")(((View)source).findViewById( " + ids[0] + "));\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
