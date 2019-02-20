package com.hacknife.briefness;

import com.hacknife.briefness.bean.Bind;
import com.hacknife.briefness.bean.Briefness;
import com.hacknife.briefness.bean.Field;
import com.hacknife.briefness.bean.Immersive;
import com.hacknife.briefness.bean.Link;
import com.hacknife.briefness.bean.Method;
import com.hacknife.briefness.bean.View;
import com.hacknife.briefness.util.ClassUtil;
import com.hacknife.briefness.util.ClassValidator;
import com.hacknife.briefness.util.Logger;
import com.hacknife.briefness.util.StringUtil;
import com.hacknife.briefness.util.ViewCollection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import static com.hacknife.briefness.XmlParser.checkedChanged;
import static com.hacknife.briefness.XmlParser.click;
import static com.hacknife.briefness.XmlParser.longclick;
import static com.hacknife.briefness.XmlParser.pageSelected;
import static com.hacknife.briefness.XmlParser.progressChanged;
import static com.hacknife.briefness.XmlParser.radioChanged;
import static com.hacknife.briefness.XmlParser.tabSelected;
import static com.hacknife.briefness.XmlParser.tabUnselected;
import static com.hacknife.briefness.XmlParser.textChanged;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class Briefnessor {
    public static final String PROXY = "Briefnessor";
    private final TypeElement typeElement;
    private final String className;//MainActivity
    private final String proxyClassName;//MainActivityBriefness
    private final String packageName;//package com.hacknife.briefness
    private Briefness briefness;
    private String javaSource;
    private String host;
    private List<String> imports = new ArrayList<>();
    private String packages;
    private String buidPath;

    public Briefnessor(Elements elementUtils, TypeElement classElement) {
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

    public String generateJavaCode(String buidPath, String packages, ProcessingEnvironment processingEnv, TypeElement typeElement) {
        if (ClassUtil.instanceOfActivity(className))
            host = "host.";
        else
            host = "view.";
        this.packages = packages;
        this.buidPath = buidPath;
        javaSource = String.format(Constant.javaPath, buidPath, packageName.replace(".", "/"), className);
        briefness = new Briefness();
        ClassParser.parser(javaSource, briefness);
        Logger.p(javaSource + ":" + briefness.getLayout());
        XmlParser.parser(buidPath, briefness.getLayout(), briefness, processingEnv, typeElement);
        return Constant.briefnessor
                .replaceAll(Constant.className, className)
                .replaceAll(Constant.iPackage, packageName)
                .replaceAll(Constant.proxy, proxyClassName)
                .replaceAll(Constant.packages, packages)
                .replaceAll(Constant.setContentView, generateContentView())
                .replaceAll(Constant.iJavabean, generateIJavabean())
                .replaceAll(Constant.iView, generateIView())
                .replaceAll(Constant.javabean, generateJavabean())
                .replaceAll(Constant.view, generateView())
                .replaceAll(Constant.findView, generateFindView())
                .replaceAll(Constant.transfer, generateTransfer())
                .replaceAll(Constant.set, generateSetBean())
                .replaceAll(Constant.clear, generateClear())
                .replaceAll(Constant.clearAll, generateClearAll())
                .replaceAll(Constant.viewModel, generateViewModel())
                .replaceAll(Constant.notify, generateNotify())
                .replaceAll(Constant.iimport, generateImport());
    }

    private String generateImport() {
        StringBuilder builder = new StringBuilder();
        for (String s : imports) {
            if (!builder.toString().contains(s))
                builder.append("import ").append(s).append(";\n");
        }
        return builder.toString();
    }

    private String generateNotify() {
        List<Link> links = briefness.getLabel().getLinkes();
        StringBuilder builder = new StringBuilder();
        builder.append("    @Override\n" +
                "    public void notifyDataChange(Class clazz) {\n" +
                "        if (clazz == null) {\n" +
                "        } ");
        for (Link link : links) {
            if (link.getAlisa().equalsIgnoreCase("viewModel"))
                continue;
            builder.append("else if (clazz == " + StringUtil.toUpperCase(link.getClassName()) + ".class) {\n" +
                    "            set" + StringUtil.toUpperCase(link.getAlisa()) + "(this." + link.getAlisa() + ");\n" +
                    "        }");
        }
        builder.append("\n    }\n");
        return builder.toString();
    }

    private String generateViewModel() {
        if (briefness.getLayout() == null) return "";
        List<Link> links = briefness.getLabel().getLinkes();
        for (Link link : links) {
            if (link.getAlisa().equalsIgnoreCase("viewModel")) {
                return "        this.viewModel = (" + link.getClassName() + ") viewModel;\n";
            }
        }
        return "";
    }

    private String generateTransfer() {
        StringBuilder builder = new StringBuilder();
        List<Method> methods = briefness.getMethods();

        for (Method method : methods) {
            String[] ids = method.getIds();
            for (String id : ids) {
                builder.append("        " + host + "findViewById(" + id + ").setOnClickListener(new View.OnClickListener() {\n" +
                        "            @Override\n" +
                        "            public void onClick(View v) {\n" +
                        "                host." + method.getMethodName() + "(v);\n" +
                        "            }\n" +
                        "        });\n");
            }
        }
        if (briefness.getLayout() != null) {
            List<View> views = briefness.getLabel().getViews();
            List<Link> links = briefness.getLabel().getLinkes();
            for (View view : views) {
                Map<String, String> transfers = view.getTransfer();
                for (Map.Entry<String, String> entry : transfers.entrySet()) {
                    Map<String, String[]> map = StringUtil.clickChangeMethod(entry.getValue(), links, views);
                    String[] method = map.get(Constant.METHOD);
                    String[] protect = map.get(Constant.PROTECT);
                    if (method.length == 0) continue;
                    if (entry.getKey().endsWith(click)) {
                        generateClick(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(longclick)) {
                        generateLongClick(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(textChanged)) {
                        generateTextChange(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(tabSelected)) {
                        generateTabSelected(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(tabUnselected)) {
                        generateTabUnselected(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(pageSelected)) {
                        generatePageSelected(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(checkedChanged)) {
                        generateCheckChange(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(radioChanged)) {
                        generateRadioChanged(builder, view.getId(), method, protect);
                    } else if (entry.getKey().endsWith(progressChanged)) {
                        generateProgressChanged(builder, view.getId(), method, protect);
                    }
                }


            }
        }
        return builder.toString();
    }

    private void generateRadioChanged(StringBuilder builder, String id, String[] method, String[] protect) {
        imports.add("com.hacknife.briefness.OnRadioButtonCheckedChangeListener");
        builder.append("        " + id + ".setOnCheckedChangeListener(new OnRadioButtonCheckedChangeListener() {\n" +
                "            @Override\n" +
                "            public void onChecked(RadioGroup radioGroup, int id) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "radioGroup, id") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "radioGroup, id") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "radioGroup, id") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "radioGroup, id") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generateProgressChanged(StringBuilder builder, String id, String[] method, String[] protect) {
        imports.add("com.hacknife.briefness.OnSeekBarChangeListener");
        imports.add("android.widget.SeekBar");
        builder.append("        " + id + ".setOnSeekBarChangeListener(new OnSeekBarChangeListener(){\n" +
                "            @Override\n" +
                "            public void onProgressChanged(SeekBar seekBar, State state, int progress, boolean fromUser) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "seekBar, state, progress, fromUser") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "seekBar, state, progress, fromUser") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "seekBar, state, progress, fromUser") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "seekBar, state, progress, fromUser") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generateTabUnselected(StringBuilder builder, String id, String[] method, String[] protect) {
        builder.append("        " + id + ".addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {\n" +
                "            @Override\n" +
                "            public void onTabReselected(TabLayout.Tab tab) {\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void onTabSelected(TabLayout.Tab tab) {\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void onTabUnselected(TabLayout.Tab tab) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "tab") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "tab") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "tab") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "tab") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generateTabSelected(StringBuilder builder, String id, String[] method, String[] protect) {
        builder.append("        " + id + ".addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {\n" +
                "            @Override\n" +
                "            public void onTabUnselected(TabLayout.Tab tab) {\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void onTabReselected(TabLayout.Tab tab) {\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void onTabSelected(TabLayout.Tab tab) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "tab") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "tab") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "tab") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "tab") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generateCheckChange(StringBuilder builder, String id, String[] method, String[] protect) {
        imports.add("com.hacknife.briefness.OnCheckBoxCheckedChangeListener");
        imports.add("android.widget.CompoundButton");
        builder.append("        " + id + ".setOnCheckedChangeListener(new OnCheckBoxCheckedChangeListener() {\n" +
                "            @Override\n" +
                "            public void onChecked(CompoundButton compoundButton, boolean checked) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "checked") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "checked") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "checked") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "checked") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generatePageSelected(StringBuilder builder, String id, String[] method, String[] protect) {
        builder.append("        " + id + ".addOnPageChangeListener(new ViewPager.OnPageChangeListener() {\n" +
                "            @Override\n" +
                "            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {\n" +
                "                \n" +
                "            }\n" +
                "\n" +
                "\n" +
                "            @Override\n" +
                "            public void onPageScrollStateChanged(int state) {\n" +
                "\n" +
                "            }\n" +
                "\n" +
                "            @Override\n" +
                "            public void onPageSelected(int position) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "position") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "position") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "position") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "position") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generateTextChange(StringBuilder builder, String id, String[] method, String[] protect) {
        imports.add("com.hacknife.briefness.TextWatcher");
        builder.append("        " + id + ".addTextChangedListener(new TextWatcher() {\n" +
                "            @Override\n" +
                "            public void onTextChange(String content) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "content") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "content") + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + StringUtil.insertParamter(method[i], "content") + "\n");
                else
                    builder.append("                host." + StringUtil.insertParamter(method[i], "content") + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private void generateLongClick(StringBuilder builder, String id, String[] method, String[] protect) {
        builder.append("        " + id + ".setOnLongClickListener(new View.OnLongClickListener() {\n" +
                "            @Override\n" +
                "            public boolean onLongClick(View v) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + method[i] + "\n");
                else
                    builder.append("                host." + method[i] + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + method[i] + "\n");
                else
                    builder.append("                host." + method[i] + "\n");
            }
        }
        builder.append("                return true;\n" +
                "            }\n" +
                "        });\n");
    }

    private void generateClick(StringBuilder builder, String id, String[] method, String[] protect) {
        builder.append("        " + id + ".setOnClickListener(new View.OnClickListener() {\n" +
                "            @Override\n" +
                "            public void onClick(View v) {\n");
        for (int i = 0; i < method.length; i++) {
            if (protect[i].length() > 0) {
                builder.append("                if(" + protect[i] + ") {\n");
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + method[i] + "\n");
                else
                    builder.append("                host." + method[i] + "\n");
                builder.append("                }\n");
            } else {
                if (StringUtil.checkMethodHavePreffix(method[i]))
                    builder.append("                " + method[i] + "\n");
                else
                    builder.append("                host." + method[i] + "\n");
            }
        }
        builder.append("            }\n" +
                "        });\n");
    }

    private String generateFindView() {
        StringBuilder builder = new StringBuilder();
        List<Field> fields = briefness.getFileds();
        for (Field field : fields) {
            if (field.getIds().length == 1)
                builder.append("        host." + field.getVariable() + " = (" + field.getClassType() + ") " + host + "findViewById(" + field.getIds()[0] + ");\n");
            else {
                builder.append("        host." + field.getVariable() + " = new " + field.getClassType() + "{\n");
                String[] ids = field.getIds();
                for (int i = 0; i < ids.length; i++) {
                    builder.append("                (" + field.getClassName() + ") " + host + "findViewById(" + ids[i] + ")");
                    if (i == ids.length - 1)
                        builder.append("\n");
                    else
                        builder.append(",\n");
                }
                builder.append("        };\n");
            }
        }
        if (briefness.getLayout() != null) {
            List<View> views = briefness.getLabel().getViews();
            for (View view : views) {
                builder.append("        " + view.getId() + " = (" + view.getClassName() + ") " + host + "findViewById(R.id." + view.getId() + ");\n");
            }
        }
        return builder.toString();
    }

    private String generateClearAll() {
        String clear = generateClear();
        if (briefness.getLayout() == null) return clear;
        List<View> views = briefness.getLabel().getViews();
        StringBuilder builder = new StringBuilder();
        builder.append(clear);
        for (View view : views) {
            builder.append("        this.").append(view.getId()).append(" = null;\n");
        }
        return builder.toString();

    }

    private String generateClear() {
        if (briefness.getLayout() == null) return "";
        List<Link> links = briefness.getLabel().getLinkes();
        StringBuilder builder = new StringBuilder();
        for (Link link : links) {
            builder.append("        if (this." + link.getAlisa() + " != null && (((Object) this." + link.getAlisa() + ")) instanceof LiveData) {\n" +
                    "            ((LiveData) ((Object) " + link.getAlisa() + ")).bindTape(null);\n" +
                    "        }\n");
            builder.append("        this.").append(link.getAlisa()).append(" = null;\n");
        }
        return builder.toString();
    }

    private String generateSetBean() {
        if (briefness.getLayout() == null) return "";
        List<Link> links = briefness.getLabel().getLinkes();
        StringBuilder builder = new StringBuilder();
        for (Link link : links) {
            if (link.getAlisa().equalsIgnoreCase("viewModel"))
                continue;
            builder.append("    public void set" + StringUtil.toUpperCase(link.getAlisa()) + "(" + StringUtil.toUpperCase(link.getClassName()) + " " + link.getAlisa() + ") {\n" +
                    "        if (" + link.getAlisa() + " == null) return;\n" +
                    "        if ((this." + link.getAlisa() + " == null || this." + link.getAlisa() + " != " + link.getAlisa() + ") && (((Object) " + link.getAlisa() + ") instanceof LiveData)) {\n" +
                    "            ((LiveData) ((Object) " + link.getAlisa() + ")).bindTape(this);\n" +
                    "        }\n" +
                    "        if (this." + link.getAlisa() + " != null && this." + link.getAlisa() + " != " + link.getAlisa() + " && (((Object) this." + link.getAlisa() + ")) instanceof LiveData) {\n" +
                    "            ((LiveData) ((Object) this." + link.getAlisa() + ")).bindTape(null);\n" +
                    "        }\n" +
                    "        this." + link.getAlisa() + " = " + link.getAlisa() + ";\n");
            List<View> views = briefness.getLabel().getViews();
            for (View view : views) {
                Bind bind = view.getBind();
                if (bind == null) continue;
                String[] methods = bind.getMethod(links);
                String[] protect = bind.getProtect(links);
                String[] alisas = bind.getAlisa(links);
                for (int i = 0; i < methods.length; i++) {
                    if (!alisas[i].equalsIgnoreCase(link.getAlisa())) continue;
                    if (protect[i].length() == 0) {
                        builder.append("        BriefnessInjector.injector(" + view.getId() + "," + methods[i] + ");\n");
                    } else {
                        builder.append("        if(" + protect[i] + ") {\n");
                        builder.append("            BriefnessInjector.injector(" + view.getId() + "," + methods[i] + ");\n");
                        builder.append("        }\n");
                    }
                }


            }
            builder.append("    }\n\n");
        }
        return builder.toString();
    }

    private String generateView() {
        if (briefness.getLayout() == null) return "";
        List<View> views = briefness.getLabel().getViews();
        StringBuilder builder = new StringBuilder();
        for (View view : views) {
            builder.append("    public ").append(view.getClassName()).append(" ").append(view.getId()).append(";\n");
        }
        return builder.toString();
    }

    private String generateJavabean() {
        if (briefness.getLayout() == null) return "";
        List<Link> links = briefness.getLabel().getLinkes();
        StringBuilder builder = new StringBuilder();
        for (Link link : links) {
            builder.append("    public ").append(link.getClassName()).append(" ").append(link.getAlisa()).append(";\n");
        }
        return builder.toString();
    }

    private String generateIView() {
        StringBuilder builder = new StringBuilder();
        List<Field> fields = briefness.getFileds();
        for (Field field : fields) {
            String fullClassName = ViewCollection.getFullNameByName(field.getClassName());
            if (fullClassName.length() != 0 && (!StringUtil.stringContainString(builder, fullClassName)))
                builder.append("import ").append(fullClassName).append(";\n");
        }
        if (briefness.getLayout() != null) {
            List<View> views = briefness.getLabel().getViews();
            for (View view : views) {
                String fullClassName = view.getFullClassName();
                if (fullClassName.length() != 0 && (!StringUtil.stringContainString(builder, fullClassName)))
                    builder.append("import ").append(fullClassName).append(";\n");
            }
        }
        return builder.toString();
    }

    private String generateIJavabean() {
        if (briefness.getLayout() == null) return "";
        List<Link> links = briefness.getLabel().getLinkes();
        StringBuilder builder = new StringBuilder();
        for (Link link : links) {
            if (!StringUtil.stringContainString(builder, link.getFullClassName()))
                builder.append("import ").append(link.getFullClassName()).append(";\n");
        }
        return builder.toString();
    }

    private String generateContentView() {
        if (ClassUtil.instanceOfActivity(className)) {
            if (briefness.getImmersive() != null) {
                ImmersiveConfig config = new ImmersiveConfig();
                config.writeCode(buidPath + "/src/main/java/" + packages.replaceAll("\\.", "/") + "/briefness/ImmersiveConfig.java", packages);
                imports.add("com.hacknife.immersive.Immersive");
                Immersive immersive = briefness.getImmersive();
                StringBuilder builder = new StringBuilder();
                builder.append("        Immersive.setContentView(host, R.layout.").append(briefness.getLayout()).append(", ");
                if (immersive.getStatusColor() != null) {
                    builder.append(immersive.getStatusColor()).append(",");
                } else {
                    imports.add(packages + ".briefness.ImmersiveConfig");
                    builder.append("ImmersiveConfig.statusColor(host)").append(",");
                }
                if (immersive.getNavigationColor() != null) {
                    builder.append(" ").append(immersive.getNavigationColor()).append(",");
                } else {
                    imports.add(packages + ".briefness.ImmersiveConfig");
                    builder.append(" ").append("ImmersiveConfig.navigationColor(host)").append(",");
                }
                if (immersive.getStatusEmbed() != null) {
                    builder.append(" ").append(immersive.getStatusEmbed()).append(",");
                } else {
                    imports.add(packages + ".briefness.ImmersiveConfig");
                    builder.append(" ").append("ImmersiveConfig.statusEmbed(host)").append(",");
                }
                if (immersive.getNavigationEmbed() != null) {
                    builder.append(" ").append(immersive.getNavigationEmbed()).append(");\n");
                } else {
                    imports.add(packages + ".briefness.ImmersiveConfig");
                    builder.append(" ").append("ImmersiveConfig.navigationEmbed(host)").append(");\n");
                }
                return builder.toString();
            } else if (briefness.getLayout() != null)
                return "        if (!Utils.contentViewExist(host)) {\n" +
                        "            host.setContentView(R.layout." + briefness.getLayout() + ");\n" +
                        "        }\n";
            else
                return "";
        } else {
            if (briefness.getLayout() == null) return "";
            return "        if (obj instanceof LayoutInflater)\n" +
                    "            view = ((LayoutInflater) obj).inflate(R.layout." + briefness.getLayout() + ", null);\n" +
                    "        else\n" +
                    "            view = (View) obj;\n";
        }
    }


}
