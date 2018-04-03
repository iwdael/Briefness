package com.aliletter.briefness;

import com.aliletter.briefness.databinding.XmlBind;
import com.aliletter.briefness.databinding.XmlViewInfo;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * e-mail : aliletter@qq.com
 * time   : 2018/04/02
 * desc   :
 * version: 1.0
 */
public class XmlProxyInfo {
    public static final String imports = "briefness:imports";
    public static final String click = "briefness:click";
    public static final String longclick = "briefness:longclick";
    public static final String action = "briefness:action";
    public static final String briefness = "briefness:";
    public static final String bind = "briefness:bind";
    public static final String id = "android:id";
    public static final String SPLIT = "\\";

    public String xml;

    private List<XmlBind> binds = new ArrayList<>();
    private List<XmlViewInfo> viewInfos = new ArrayList<>();


    public XmlProxyInfo(String path) {

        try {
            xml = System.getProperty("user.dir") + SPLIT + "example" + SPLIT + "src" + SPLIT + "main" + SPLIT + "res" + SPLIT + "layout" + SPLIT + path + ".xml";
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
                            if (parser.getAttributeName(i).contains(briefness) && (!parser.getAttributeName(i).equalsIgnoreCase(imports))) {
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
                                System.out.print(name + "\n");
                                if (name.equalsIgnoreCase(id)) {
                                    info.ID = id2String(value);
                                }
                                if (name.equalsIgnoreCase(click)) {
                                    info.click = value;
                                }
                                if (name.equalsIgnoreCase(longclick)) {
                                    info.longClick = value;
                                }
                                if (name.equalsIgnoreCase(action)) {
                                    info.action = value;
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
        } catch (Exception e) {
        }

    }

    private String bind2String(String source) {
        System.out.print("---------------------------------------------------------------->>\n");
        System.out.print(source + "\n");

        StringBuilder builder = new StringBuilder();
        if (source.endsWith(";")) {
            String[] binds = source.split(";");
            for (String bind : binds) {
                int start = bind.indexOf("@{") + 2;
                int end = bind.indexOf("}");
                String var = bind.substring(start, end);
                System.out.print(var + "\n");
                String startStr = var.substring(0, var.lastIndexOf("."));
                String endStr = var.substring(var.lastIndexOf(".") + 1);
                System.out.print(startStr + "\n");
                System.out.print(endStr + "\n");
                String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";
                System.out.print(method + "\n");
                builder.append(bind.substring(0, start - 2)).append(method).append(bind.substring(end + 1, bind.length())).append(";");
            }
        } else {
            String bind = source;
            int start = bind.indexOf("@{") + 2;
            int end = bind.indexOf("}");
            String var = bind.substring(start, end);
            System.out.print(var + "\n");
            String startStr = var.substring(0, var.lastIndexOf("."));
            String endStr = var.substring(var.lastIndexOf(".") + 1);
            System.out.print(startStr + "\n");
            System.out.print(endStr + "\n");

            String method = startStr + ".get" + endStr.substring(0, 1).toUpperCase() + endStr.substring(1) + "()";
            System.out.print(method + "\n");
            builder.append(bind.substring(0, start - 2)).append(method).append(bind.substring(end + 1, bind.length()));
            if (!source.endsWith("}")) builder.append(";");
        }

        System.out.print(builder.toString() + "\n");
        System.out.print("---------------------------------------------------------------->>\n");
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
}
