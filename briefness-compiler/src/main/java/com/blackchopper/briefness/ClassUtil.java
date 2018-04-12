package com.blackchopper.briefness;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;

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

    public static boolean instanceOfActivity(String qualifiedName) {
        String prevrious = qualifiedName;
        String current = getSuperClass(qualifiedName);
        while (true) {
            if (prevrious.equalsIgnoreCase(current)) {
                break;
            } else {

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

    public static String findLayoutById(String clazz) {
        String packageName = findPackageName();
        String module = readTextFile(System.getProperty("user.dir") + "/BriefnessConfig");
        if (packageName.length() == 0) return "";
        String R = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT
                + "src/main/java/"  + clazz.replace(".","/") + ".java";
        R = R.replace("\\", "/");


        String content = readTextFile(R).replace(" ", "");

        int start = content.indexOf("@BindLayout(R.layout.");
        if (start == -1) return "";
        int end = start;
        for (int i = end; i < content.length(); i++) {
            if (content.charAt(i) == ')') {
                end = i;
            }
        }
        String layout = content.substring(start + 21, end);

        return layout;
    }


    public static String findPackageName() {
        String packageName = "";
        String module = readTextFile(System.getProperty("user.dir") + "/BriefnessConfig");
        String xml = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT + "src" + SPLIT + "main" + SPLIT + "AndroidManifest.xml";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 获得xml解析类的引用
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new FileReader(xml));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equalsIgnoreCase("package")) {
                                packageName = parser.getAttributeValue(i);
                            }
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return packageName;
    }
}
