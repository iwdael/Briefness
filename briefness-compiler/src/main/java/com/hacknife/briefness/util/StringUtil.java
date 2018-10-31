package com.hacknife.briefness.util;

import com.hacknife.briefness.Constant;

import java.io.File;

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

    public static String[] clickChangeMethod(String click) {
        if (click == null || click.trim().length() == 0) return new String[0];
        if (click.endsWith(";") || click.endsWith(")")) {
            String[] methods = click.split(";");
            String[] result = new String[methods.length];
            for (int i = 0; i < methods.length; i++) {
                result[i] = click2Method(methods[i]);
            }
            return result;
        } else {
            return new String[]{click + "();"};
        }
    }

    private static String click2Method(String click) {
        if (click.contains("$")) {
            int start = click.indexOf("(");
            int end = click.lastIndexOf(")");
            String[] params = click.substring(start + 1, end).split(",");
            StringBuilder builder = new StringBuilder();
            builder.append(click.substring(0, start + 1));
            for (int i = 0; i < params.length; i++) {
                if (!params[i].contains("$")) {
                    builder.append(params[i]);
                } else {
                    if (!params[i].contains(".")) {
                        builder.append(toTextValue(params[i]));
                    } else {
                        builder.append(variable2Method(params[i]));
                    }
                }
                if (i < params.length - 1) {
                    builder.append(",");
                }
            }
            builder.append(");");
            return builder.toString();
        } else {
            return click+";";
        }
    }

    private static String variable2Method(String variable) {
        int start = variable.indexOf("$");
        int end = variable.lastIndexOf("$");
        String var = variable.substring(start + 1, end);
        int split = var.indexOf(".");
        String entity = var.substring(0, split);
        String field = var.substring(split + 1, var.length());
        StringBuilder builder = new StringBuilder();
        builder.append(entity).append(".get").append(toUpperCase(field)).append("()");
        return builder.toString();
    }

    private static String toTextValue(String textView) {
        int start = textView.indexOf("$");
        int end = textView.lastIndexOf("$");
        String view = textView.substring(start + 1, end);
        StringBuilder builder = new StringBuilder();
        builder.append(view).append(".getText().toString().trim()");
        return builder.toString();
    }

    public static void main(String[] a) {
        System.out.print(variable2Method("$entity.username$"));
    }
}
