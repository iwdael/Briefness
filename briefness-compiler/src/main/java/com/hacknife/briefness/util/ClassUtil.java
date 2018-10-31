package com.hacknife.briefness.util;

import java.io.File;

import static com.hacknife.briefness.XmlInfo.SPLIT;
import static com.hacknife.briefness.XmlInfo.readTextFile;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public class ClassUtil {


    public static boolean instanceOfActivity(String qualifiedName) {
        if (qualifiedName.contains("Activity"))
            return true;
        else
            return false;
    }


    public static String findLayoutById(String clazz, String modulePath) {

        String R = modulePath + SPLIT
                + "src/main/java/" + clazz.replace(".", "/") + ".java";
        R = R.replace("\\", "/");
        String content = readTextFile(R).replace(" ", "");
        int start = content.indexOf("@BindLayout(R.layout.");
        int differ = 21;
        if (start == -1) {
            start = content.indexOf("@BindLayout(R2.layout.");
            differ = 22;
        }
        if (start == -1) return "";
        int end = start;
        for (int i = end; i < content.length(); i++) {
            if (content.charAt(i) == ')') {
                end = i;
                break;
            }
        }
        String layout = content.substring(start + differ, end);
        return layout;
    }
}