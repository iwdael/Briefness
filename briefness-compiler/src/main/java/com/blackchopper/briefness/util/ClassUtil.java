package com.blackchopper.briefness.util;

import com.blackchopper.briefness.XmlInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileReader;

import static com.blackchopper.briefness.XmlInfo.SPLIT;
import static com.blackchopper.briefness.XmlInfo.findMainModule;
import static com.blackchopper.briefness.XmlInfo.readTextFile;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

public class ClassUtil {


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

    public static String getSuperClass(String qualifiedName) {
        if (qualifiedName.startsWith("android.")) {
            return qualifiedName;
        }
        String module = findMainModule() + SPLIT;
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

    public static String findLayoutById(String clazz) {
        String packageName = findPackageName();

        if (packageName.length() == 0) return "";
        String R =findMainModule() + SPLIT
                + "src/main/java/" + clazz.replace(".", "/") + ".java";
        R = R.replace("\\", "/");
        String content = readTextFile(R).replace(" ", "");
        int start = content.indexOf("@BindLayout(R.layout.");
        if (start == -1) return "";
        int end = start;
        for (int i = end; i < content.length(); i++) {
            if (content.charAt(i) == ')') {
                end = i;
                break;
            }
        }
        String layout = content.substring(start + 21, end);
        return layout;
    }

    public static String findPackageName() {
        String packageName = "";

        String xml = findMainModule() + SPLIT + "src" + SPLIT + "main" + SPLIT + "AndroidManifest.xml";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

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

    public static String findViewById(String clazz, String fieldName) {
        String packageName = findPackageName();

        if (packageName.length() == 0) return "";
        String R =findMainModule() + SPLIT
                + "src/main/java/" + clazz.replace(".", "/") + ".java";
        R = R.replace("\\", "/");
        String content = readTextFile(R).replace(" ", "");
        String cont = content.substring(0, content.indexOf(fieldName + ";"));
        return cont.substring(cont.lastIndexOf("@BindView(") + 10, cont.lastIndexOf(")"));
    }

    public static String[] findViewsById(String clazz, String fieldName) {
        String packageName = findPackageName();

        if (packageName.length() == 0) return new String[]{};
        String R = findMainModule()+ SPLIT
                + "src/main/java/" + clazz.replace(".", "/") + ".java";
        R = R.replace("\\", "/");
        String content = readTextFile(R).replace(" ", "");
        String cont = content.substring(0, content.indexOf(fieldName + ";"));
        return cont.substring(cont.lastIndexOf("@BindViews({") + 12, cont.lastIndexOf("}")).split(",");
    }
}
