package com.blackchopper.briefness;

import com.blackchopper.briefness.databinding.XmlBind;
import com.blackchopper.briefness.databinding.XmlViewInfo;
import com.blackchopper.briefness.util.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public class XmlInfo {
    public static final String imports = "briefness:imports";
    public static final String click = "briefness:click";
    public static final String longclick = "briefness:longclick";
    public static final String touch = "briefness:touch";
    public static final String action = "briefness:action";
    public static final String briefness = "briefness:";
    public static final String bind = "briefness:bind";
    public static final String id = "android:id";
    public static final String include = "include";
    public static final String layout = "layout";
    public static final String SPLIT = "/";

    public String xml;

    private List<XmlBind> binds = new ArrayList<>();
    private List<XmlViewInfo> viewInfos = new ArrayList<>();


    public XmlInfo(String path) {
        xml = findMainModule() + SPLIT + "src" + SPLIT + "main" + SPLIT + "res" + SPLIT + "layout" + SPLIT + path + ".xml";
        parserXml(path);

    }

    public static String readTextFile(String path) {
        StringBuffer sb = new StringBuffer();
        try {
            File file = new File(path);
            if (!file.exists() || file.isDirectory())
                throw new FileNotFoundException();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String temp = null;

            temp = br.readLine();
            while (temp != null) {
                sb.append(temp + " ");
                temp = br.readLine();
            }

        } catch (Exception e) {

        }
        return sb.toString().replace(" ", "");
    }

    public static String findMainModule() {
        File dir = new File(System.getProperty("user.dir") + SPLIT);
        Logger.v("dir:" + dir.getAbsolutePath());
        File[] modules = dir.listFiles();
        for (File module : modules) {
            if (!module.isDirectory()) continue;
            if (!new File(module.getAbsoluteFile() + "/build.gradle").exists()) continue;
            if (readTextFile(module.getAbsolutePath() + "/build.gradle").replace(" ", "").contains("applyplugin:'com.android.application'")) {
                Logger.v("main module: " + module.getAbsolutePath());
                return module.getAbsolutePath();
            }
        }
        return "";
    }

    public static String specialBind2String(String source) {
        int start = source.indexOf('$') + 1;
        int end = source.lastIndexOf("$");

        String methodSource = source.substring(start, end);

        int dot = methodSource.indexOf(".");

        String method = methodSource.substring(0, dot) + ".get(\"" + methodSource.substring(dot + 1, methodSource.length()) + "\")";

        String result = source.substring(0, start - 1) + method + source.substring(end + 1, source.length());

        return result;
    }

    public static void main(String[] a) {
        specialBind2String("$user.info$");
    }

    private void parserXml(String path) {
        try {
            String xmlName = findMainModule() + SPLIT + "src" + SPLIT + "main" + SPLIT + "res" + SPLIT + "layout" + SPLIT + path + ".xml";

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 获得xml解析类的引用
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new FileReader(xmlName));
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:
                        boolean have = false;
                        int count = parser.getAttributeCount();
                        //获取引用
                        for (int i = 0; i < count; i++) {
                            if (parser.getAttributeName(i).equalsIgnoreCase(imports)) {
                                String[] imports = parser.getAttributeValue(i).split(";");
                                for (String anImport : imports) {
                                    String[] na = anImport.split(",");
                                    if (na.length == 2) binds.add(new XmlBind(na[0], na[1], null));
                                    else binds.add(new XmlBind(na[0], na[1], na[2]));
                                }
                            }
                            if (parser.getAttributeName(i).contains(id) | parser.getName().contains(include)) {
                                have = true;
                            }
                        }

                        //获取信息
                        if (have) {
                            XmlViewInfo info = new XmlViewInfo();
                            info.view = parser.getName();
                            for (int i = 0; i < count; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (name.equalsIgnoreCase(id)) {
                                    info.ID = id2String(value);
                                }
                                if (name.equalsIgnoreCase(click)) {
                                    info.click = click2String(value);
                                }
                                if (name.equalsIgnoreCase(longclick)) {
                                    info.longClick = click2String(value);
                                }
                                if (name.equalsIgnoreCase(touch)) {
                                    info.touch = value;
                                }
                                if (name.equalsIgnoreCase(bind)) {
                                    info.bind = bind2String(value);
                                    info.bindSource = value;
                                }
                                if (name.equalsIgnoreCase(action)) {
                                    info.action = value;
                                }
                                if (name.equalsIgnoreCase(layout)) {
                                    parserXml(value.replace("@layout/", ""));
                                }
                            }
                            if (!info.view.equalsIgnoreCase(include))
                                viewInfos.add(info);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String id2String(String source) {
        String target = null;
        if (source.contains("@+id/")) {
            target = source.substring(source.indexOf("@+id/") + 5, source.length());
        }
        if (source.contains("@id/")) {
            target = source.substring(source.indexOf("@id/") + 4, source.length());
        }
        return target;
    }

    private String click2String(String source) {
        StringBuilder builder = new StringBuilder();
        if (source.endsWith(";")) {
            String[] clicks = source.split(";");
            for (String click : clicks) {
                if (click.contains("$")) {
                    int index = 0;
                    while (true) {
                        int start = click.indexOf("$", index) + 1;
                        if (start == -1) break;
                        int end = click.indexOf("$", start);
                        if (end == -1) break;
                        index = end + 1;
                        String var = click.substring(start, end);
                        if (var.lastIndexOf(".") == -1) {
                            click = click.substring(0, start - 1) + var + ".getText().toString()" + click.substring(end + 1, click.length());
//                            builder.append().append().append().append(";");
                        } else {
                            String startStr = var.substring(0, var.lastIndexOf("."));
                            String endStr = var.substring(var.lastIndexOf(".") + 1);
                            String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";
//                            builder.append(click.substring(0, start - 1)).append(method).append(click.substring(end + 1, click.length())).append(";");
                            click = click.substring(0, start - 1) + method + click.substring(end + 1, click.length());
                        }
                    }
                }
                builder.append(click).append(";");
            }
        } else {
            builder.append(source).append(";");
        }
        return builder.toString();
    }

    private String bind2String(String source) {
        StringBuilder builder = new StringBuilder();
        if (source.endsWith(";")) {
            String[] binds = source.split(";");
            for (String bind : binds) {
                if (bind.contains("$")) {
                    int start = bind.indexOf("$") + 1;
                    int end = bind.indexOf("$", start);
                    String setMethod = bind.substring(start, end);
                    boolean havaList = setMethod.contains("[");
                    String index = null;
                    if (havaList) {
                        index = setMethod.substring(setMethod.indexOf("[") + 1, setMethod.indexOf("]"));
                    }
                    String startStr = setMethod.substring(0, setMethod.lastIndexOf("."));
                    String endStr;
                    if (havaList) {
                        endStr = setMethod.substring(setMethod.lastIndexOf(".") + 1, setMethod.indexOf("["));
                    } else {
                        endStr = setMethod.substring(setMethod.lastIndexOf(".") + 1);
                    }
                    String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";
                    if (havaList)
                        method = method + ".get(" + index + ")";
                    builder.append(bind.substring(0, start - 1)).append(method).append(bind.substring(end + 1, bind.length())).append(";");
                } else {
                    builder.append(bind).append(";");
                }
            }
        } else {
            String bind = source;
            int start = bind.indexOf("$") + 1;
            int end = bind.indexOf("$", start);
            String setMethod = bind.substring(start, end);
            boolean havaList = setMethod.contains("[");
            String index = null;
            if (havaList) {
                index = setMethod.substring(setMethod.indexOf("[") + 1, setMethod.indexOf("]"));
            }
            String startStr = setMethod.substring(0, setMethod.lastIndexOf("."));
            String endStr;
            if (havaList) {
                endStr = setMethod.substring(setMethod.lastIndexOf(".") + 1, setMethod.indexOf("["));
            } else {
                endStr = setMethod.substring(setMethod.lastIndexOf(".") + 1);
            }
            String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";
            if (havaList)
                method = method + ".get(" + index + ")";
            builder.append(bind.substring(0, start - 1)).append(method).append(bind.substring(end + 1, bind.length()));
            if (!source.endsWith("$")) builder.append(";");
        }

        return builder.toString();
    }

    public List<XmlBind> getBinds() {
        List<XmlBind> list = new ArrayList<>();
        for (XmlBind xmlBind : binds) {
            for (XmlViewInfo viewInfo : viewInfos) {
                if (viewInfo.bind != null) {
                    if (viewInfo.bind.contains(xmlBind.name)) {
                        xmlBind.list.add(viewInfo);
                    }
                }
            }
            list.add(xmlBind);
        }
        return list;
    }

    public List<XmlViewInfo> getViewInfos() {
        return viewInfos;
    }

}
