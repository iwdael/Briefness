package com.hacknife.briefness.util;



import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;

import static com.hacknife.briefness.XmlInfo.SPLIT;
import static com.hacknife.briefness.XmlInfo.readTextFile;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public class ClassUtil {


    public static boolean instanceOfActivity(String qualifiedName, String modulePath) {
        if (qualifiedName.contains("Activity")) return true;
        String prevrious = qualifiedName;
        String current = getSuperClass(qualifiedName, modulePath);
        while (true) {
            if (prevrious.equalsIgnoreCase(current)) {
                break;
            } else {
                prevrious = current;
                current = getSuperClass(current, modulePath);
            }
        }
        if (current.startsWith("android.")) {
            if (current.contains("Activity")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }

    public static String getSuperClass(String qualifiedName, String modulePath) {
        if (qualifiedName.startsWith("android.")) {
            return qualifiedName;
        }
        String module = modulePath + SPLIT;
        String path = qualifiedName.substring(0, qualifiedName.lastIndexOf(".")).replace(".", SPLIT);
        String clazzName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
        String file = module + "src/main/java/" + path + SPLIT + clazzName + ".java";
        file = file.replace("/", "\\");
        if (!new File(file).exists()) {
            return qualifiedName;
        }
        String content = readTextFile(file);
        if (!content.contains("class" + clazzName)) {
            return qualifiedName;
        }
        int start = content.indexOf("class" + clazzName);
        int end = content.indexOf("{");
        String title = content.substring(start, end);

        if (title.contains("<") & title.contains(">")) {
            int startIndex = title.indexOf("<");
            int endIndex = title.lastIndexOf(">");
            title = title.substring(0, startIndex) + title.substring(endIndex + 1);
        }
        title = title.substring(title.indexOf("extends") + 7);
        if (title.contains("implements")) {
            title = title.substring(0, title.indexOf("implements"));
        }
        String[] splits = content.split(";");

        String superClass = null;
        for (String split : splits) {
            if (split.endsWith(title)) {
                superClass = split.replace("import", "").replace(";", "");
            }
        }
        if (superClass == null) {
            superClass = qualifiedName.substring(0, qualifiedName.lastIndexOf(".")) + "." + title;
        }
        return superClass;
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