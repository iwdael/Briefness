package com.hacknife.briefness.util;

import com.hacknife.briefness.Constant;
import com.hacknife.briefness.bean.Link;
import com.hacknife.briefness.bean.View;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class StringUtil {
    public static String toUpperCase(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public static String findPackage(String annotationFilePath) {
        File annotationFile = new File(annotationFilePath);
        File dir = annotationFile.getParentFile();
        while (!(dir.getAbsolutePath().endsWith(Constant.debug) || dir.getAbsolutePath().endsWith(Constant.release))) {
            String dirPath = dir.getAbsolutePath();
            dirPath = reverse(dirPath).replaceFirst(Constant.tpa, "gifnoCdliub");
            dirPath = reverse(dirPath);
            File R = new File(dirPath + File.separator + "BuildConfig.java");
            if (R.exists()) {
                dir = new File(dirPath);
                StringBuilder builder = new StringBuilder();
                while (!(dir.getAbsolutePath().endsWith(Constant.debug) || dir.getAbsolutePath().endsWith(Constant.release))) {
                    builder.insert(0, Constant.dot + dir.getName());
                    dir = dir.getParentFile();
                }
                return builder.toString().substring(1);
            }
            dir = dir.getParentFile();
        }
        return null;
    }


    public static String reverse(String string) {
        StringBuilder builder = new StringBuilder();
        builder.append(string);
        return builder.reverse().toString();
    }

    public static String findBuildDir(String path) {
        File file = new File(path);
        while (!file.getParentFile().getPath().endsWith(Constant.build)) {
            file = file.getParentFile();
        }
        return file.getParentFile().getParentFile().getPath();
    }


    public static boolean stringContainString(StringBuilder builder, String string) {
        if (builder.toString().contains(string))
            return true;
        else
            return false;
    }

    public static Map<String, String[]> clickChangeMethod(String click, List<Link> links, List<View> views) {
        Map<String, String[]> map = new HashMap<>();
        if (click == null || click.trim().length() == 0) {
            map.put(Constant.METHOD, new String[0]);
            map.put(Constant.PROTECT, new String[0]);
            return map;
        }
        if (click.contains(";") || click.endsWith(")") || click.contains("|")) {
            String[] methods;
            if (click.contains("|")) {
                methods = click.split("\\|");
            } else {
                methods = click.split(";");
            }
            String[] protect = new String[methods.length];
            String[] result = new String[methods.length];
            for (int i = 0; i < methods.length; i++) {
                StringBuilder protectBuilder = new StringBuilder();
                if (!methods[i].endsWith(")")) {
                    result[i] = methods[i] + "();";
                } else {
                    result[i] = click2Method(methods[i], links, protectBuilder, views);
                }
                protect[i] = protectBuilder.toString();
            }
            map.put(Constant.METHOD, result);
            map.put(Constant.PROTECT, protect);
            return map;
        } else {
            map.put(Constant.METHOD, new String[]{click + "();"});
            map.put(Constant.PROTECT, new String[]{""});
            return map;
        }
    }

    private static String click2Method(String click, List<Link> links, StringBuilder protectBuilder, List<View> views) {
        if (click.contains("$")) {
            int start = click.indexOf("(");
            int end = click.lastIndexOf(")");
            StringBuilder builder = new StringBuilder();
            builder.append(click.substring(0, start + 1));
            String[] params = click.substring(start + 1, end).split(",");
            for (int i = 0; i < params.length; i++) {
                String param = params[i];
                StringBuilder protect = new StringBuilder();
                builder.append(parameter2Method(param, links, protect, views));
                if (i != params.length - 1)
                    builder.append(" , ");
                if (protectBuilder.length() != 0 && protect.length() != 0)
                    protectBuilder.append(" && ");
                protectBuilder.append(protect);
            }

            builder.append(");");
            return builder.toString();
        } else {
            return click + ";";
        }
    }

    public static String parameter2Method(String variable, List<Link> links, StringBuilder protect, List<View> views) {
        if (!variable.contains("$")) return variable;
        StringBuilder builder = new StringBuilder();
        int start = variable.indexOf("$") + 1;
        int end = variable.indexOf("$", start);
        builder.append(variable.substring(0, start - 1));
        String par = variable.substring(start, end);
        if (par.contains(".")) {
            par = par.replaceAll(" ", "");
            int split = par.indexOf(".");
            String entity = par.substring(0, split);
            String field = par.substring(split + 1, par.length());
            String index = null;
            if (field.contains("[") && field.contains("]")) {
                int fieldIndex = field.indexOf("[");
                index = field.substring(fieldIndex + 1, field.length() - 1);
                field = field.substring(0, fieldIndex);
            }
            int type = 1;//自定义javabean，map,bundle;
            for (Link link : links) {
                Map map = new HashMap();
                map.get("");
                if (link.getAlisa().equalsIgnoreCase(entity) && link.getFullClassName().equalsIgnoreCase("android.os.Bundle")) {
                    type = 2;
                    break;
                }
                if (link.getAlisa().equalsIgnoreCase(entity) && link.getFullClassName().equalsIgnoreCase("java.util.Map")) {
                    type = 2;
                }
            }
            if (type == 1) {
                builder.append(entity + ".get" + StringUtil.toUpperCase(field) + "()");
            } else if (type == 2) {
                builder.append(entity + ".get(\"" + field + "\")");
            }
            if (index != null) {
                protect.append(entity + " != null && " + entity + ".get" + StringUtil.toUpperCase(field) + "() != null && " + entity + ".get" + StringUtil.toUpperCase(field) + "().size()>" + index);
                builder.append(".get(" + index + ")");
            }
        } else {
            for (View view : views) {
                if (view.getId().equals(par)) {
                    builder.append(par).append(view.getValue());
                    break;
                }
            }
        }
        builder.append(variable.substring(end + 1, variable.length()));
        String var = builder.toString();
        if (var.contains("$")) {
            if (var.contains("[") && var.contains("]")) {
                protect.append(" && ");
            }
            return parameter2Method(var, links, protect, views);
        } else {
            return var;
        }
    }

    public static String variable2Method(String variable, List<Link> links, StringBuilder protect) {
        if (!variable.contains("$")) return variable;
        StringBuilder builder = new StringBuilder();
        int start = variable.indexOf("$") + 1;
        int end = variable.indexOf("$", start);
        builder.append(variable.substring(0, start - 1));
        String par = variable.substring(start, end);
        if (par.contains(".")) {
            par = par.replaceAll(" ", "");
            int split = par.indexOf(".");
            String entity = par.substring(0, split);
            String field = par.substring(split + 1, par.length());
            String index = null;
            if (field.contains("[") && field.contains("]")) {
                int fieldIndex = field.indexOf("[");
                index = field.substring(fieldIndex + 1, field.length() - 1);
                field = field.substring(0, fieldIndex);
            }
            int type = 1;//自定义javabean，map,bundle;
            for (Link link : links) {
                Map map = new HashMap();
                map.get("");
                if (link.getAlisa().equalsIgnoreCase(entity) && link.getFullClassName().equalsIgnoreCase("android.os.Bundle")) {
                    type = 2;
                    break;
                }
                if (link.getAlisa().equalsIgnoreCase(entity) && link.getFullClassName().equalsIgnoreCase("java.util.Map")) {
                    type = 2;
                }
            }
            if (type == 1) {
                builder.append(entity + ".get" + StringUtil.toUpperCase(field) + "()");
            } else if (type == 2) {
                builder.append(entity + ".get(\"" + field + "\")");
            }
            if (index != null) {
                protect.append(entity + " != null && " + entity + ".get" + StringUtil.toUpperCase(field) + "() != null && " + entity + ".get" + StringUtil.toUpperCase(field) + "().size()>" + index);
                builder.append(".get(" + index + ")");
            }
        } else {
            builder.append(par + ".getText().toString().trim()");
        }
        builder.append(variable.substring(end + 1, variable.length()));
        String var = builder.toString();
        if (var.contains("$")) {
            if (var.contains("[") && var.contains("]")) {
                protect.append(" && ");
            }
            return variable2Method(var, links, protect);
        } else {
            return var;
        }
    }


    public static void main(String[] a) {
//        List<Link> links = new ArrayList<>();
//        links.add(new Link("android.os.Bundle", "bundle"));
//        links.add(new Link("java.util.Map", "map"));
//        links.add(new Link("com.hacknife.demo.User", "user"));
//        String var = "onListener";
//        Map<String, String[]> map = clickChangeMethod(var, links);
//        String[] methods = map.get("METHOD");
//        String[] protects = map.get("PROTECT");
//        for (int i = 0; i < methods.length; i++) {
//            System.out.print("method:-->>" + methods[i] + "\n\n");
//            System.out.print("protect:-->>" + protects[i] + "\n\n");
//        }
        String str = "$user.username$|$user.pswd$";
        String split[] = str.split("\\|");
        for (int i = 0; i < split.length; i++) {
            System.out.print(split[i] + "\n");
        }
    }


    public static int charCount(String str, String find) {
        int index = 0;
        int count = 0;
        while ((index = str.indexOf(find, index)) != -1) {
            index += find.length();
            count++;
        }
        return count;
    }

    public static boolean checkMethodHavePreffix(String method) {
        int index = method.indexOf("(");
        return method.substring(0, index).contains(".");
    }

    public static String methodPreffixProtect(String method) {
        int index = method.indexOf("(");
        index = method.substring(0, index).indexOf(".");
        return "if(" + method.substring(0, index) + " != null) ";
    }

    public static String insertParamter(String method, String para) {
        StringBuilder builder = new StringBuilder();
        int index = method.indexOf("(") + 1;
        int lastIndex = method.indexOf(")");
        builder.append(method.subSequence(0, index));
        return builder.append(para).append(index == lastIndex ? "" : ", ").append(method.subSequence(index, method.length())).toString();
    }
}
