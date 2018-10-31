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
}
