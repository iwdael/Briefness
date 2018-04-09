package com.blackchopper.briefness;

import com.blackchopper.briefness.databinding.XmlViewInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import com.blackchopper.briefness.databinding.XmlBind;


/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public class XmlProxyInfo {
    public static final String imports = "briefness:imports";
    public static final String click = "briefness:click";
    public static final String longclick = "briefness:longclick";
    public static final String touch = "briefness:touch";
    public static final String briefness = "briefness:";
    public static final String bind = "briefness:bind";
    public static final String id = "android:id";
    public static final String SPLIT = "/";
    public String packageName;
    public String module;

    public String xml;

    private List<XmlBind> binds = new ArrayList<>();
    private List<XmlViewInfo> viewInfos = new ArrayList<>();


    public XmlProxyInfo(String path) {

        try {
            module = readTextFile(System.getProperty("user.dir") + "/BriefnessConfig");
            xml = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT + "src" + SPLIT + "main" + SPLIT + "res" + SPLIT + "layout" + SPLIT + path + ".xml";
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            // 获得xml解析类的引用
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new FileReader(xml));
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
//                            if (parser.getAttributeName(i).contains(briefness) && (!parser.getAttributeName(i).equalsIgnoreCase(imports))) {
//                                have = true;
//                            }
                            if (parser.getAttributeName(i).contains(id)){
                                have=true;
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
                                    info.click = value;
                                }
                                if (name.equalsIgnoreCase(longclick)) {
                                    info.longClick = value;
                                }
                                if (name.equalsIgnoreCase(touch)) {
                                    info.touch = value;
                                }
                                if (name.equalsIgnoreCase(bind)) {
                                    info.bind = bind2String(value);
                                }
                            }
                            viewInfos.add(info);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();

            }

            xml = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT + "src" + SPLIT + "main" + SPLIT + "AndroidManifest.xml";
            factory = XmlPullParserFactory.newInstance();
            // 获得xml解析类的引用
            parser = factory.newPullParser();
            parser.setInput(new FileReader(xml));
            eventType = parser.getEventType();
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
        }

    }

    private String bind2String(String source) {


        StringBuilder builder = new StringBuilder();
        if (source.endsWith(";")) {
            String[] binds = source.split(";");
            for (String bind : binds) {
                int start = bind.indexOf("@{") + 2;
                int end = bind.indexOf("}");
                String var = bind.substring(start, end);

                String startStr = var.substring(0, var.lastIndexOf("."));
                String endStr = var.substring(var.lastIndexOf(".") + 1);

                String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";

                builder.append(bind.substring(0, start - 2)).append(method).append(bind.substring(end + 1, bind.length())).append(";");
            }
        } else {
            String bind = source;
            int start = bind.indexOf("@{") + 2;
            int end = bind.indexOf("}");
            String var = bind.substring(start, end);

            String startStr = var.substring(0, var.lastIndexOf("."));
            String endStr = var.substring(var.lastIndexOf(".") + 1);


            String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";

            builder.append(bind.substring(0, start - 2)).append(method).append(bind.substring(end + 1, bind.length()));
            if (!source.endsWith("}")) builder.append(";");
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
        return sb.toString();
    }


}
