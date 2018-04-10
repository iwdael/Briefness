package com.blackchopper.briefness;

import static com.blackchopper.briefness.XmlProxyInfo.SPLIT;
import static com.blackchopper.briefness.XmlProxyInfo.readTextFile;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

public class ClassUtil {

    public static String getSuperClass(String qualifiedName) {
        if (qualifiedName.startsWith("android.")) {
            return qualifiedName;
        }
        String module = System.getProperty("user.dir") + readTextFile(System.getProperty("user.dir") + "/BriefnessConfig") + SPLIT;
        String path = qualifiedName.substring(0, qualifiedName.lastIndexOf(".")).replace(".", SPLIT);
        String clazzName = qualifiedName.substring(qualifiedName.lastIndexOf(".") + 1);
        String file = module + "src/main/java/" + path + SPLIT + clazzName + ".java";
        file = file.replace("/", "\\");
        System.out.print(file + "\n");
        String content = readTextFile(file);
        if (!content.contains("class" + clazzName + "extends")) {
            return qualifiedName;
        }
        int start = content.indexOf("class" + clazzName + "extends");
        int end = content.indexOf("{");
        String title = content.substring(start, end);
        title = title.substring(title.indexOf("extends") + 7);
        if (title.contains("implements")) {
            title = title.substring(0, title.indexOf("implements"));
        }
        System.out.print(title + "\n");
        String[] splits = content.split(";");
        System.out.print("----len--->>" + splits.length + "\n");
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

    public static boolean instanceOfActivity(String qualifiedName) {
        String prevrious = qualifiedName;
        String current = getSuperClass(qualifiedName);
        while (true) {
            if (prevrious.equalsIgnoreCase(current)) {
                break;
            } else {
                System.out.print("------>>" + prevrious + " ----\n");
                System.out.print("------>>" + current + " ----\n");
                prevrious = current;
                current = getSuperClass(current);
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
}
