package com.hacknife.briefness;

import com.hacknife.briefness.bean.Briefness;
import com.hacknife.briefness.bean.Link;
import com.hacknife.briefness.bean.View;
import com.hacknife.briefness.util.Logger;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class XmlParser {
    public static final String imports = "imports";
    public static final String click = "click";
    public static final String longclick = "longClick";
    public static final String touch = "touch";
    public static final String bind = "bind";
    public static final String action = "action";
    public static final String id = "android:id";

    public static final String SPLIT = "/";
    public static final String include = "include";
    public static final String layout = "layout";


    public static void parser(String buidDir, String layoutName, Briefness briefness) {
        if (layoutName == null) return;
        String xmlName = buidDir + SPLIT + "src" + SPLIT + "main" + SPLIT + "res" + SPLIT + "layout" + SPLIT + layoutName + ".xml";
        if (!new File(xmlName).exists()) {
            Logger.v("not found xml :" + xmlName);
            return;
        }
        try {
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
                            if (parser.getAttributeName(i).contains(imports)) {
                                String[] links = parser.getAttributeValue(i).replaceAll(" ", "").split(";");
                                for (String link : links) {
                                    String[] split = link.split(",");
                                    Link aLink = new Link(split[0], split[1]);
                                    briefness.getLabel().addLink(aLink);
                                }
                            }
                            if (parser.getAttributeName(i).contains(id) | parser.getName().contains(include)) {
                                have = true;
                            }
                        }
                        //获取信息
                        if (have) {
                            View view = new View();
                            view.setClassName(parser.getName());
                            for (int i = 0; i < count; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (name.equalsIgnoreCase(id)) {
                                    view.setId(id2String(value));
                                }
                                if (name.contains(click)) {
                                    view.setClick(deleteBlank(value));
                                }
                                if (name.contains(longclick)) {
                                    view.setLongClick(deleteBlank(value));
                                }
                                if (name.contains(touch)) {
                                    view.setTouch(deleteBlank(value));
                                }
                                if (name.contains(bind)) {
                                    view.setBind(deleteBlank(value));
                                }
                                if (name.contains(action)) {
                                    view.setAction(deleteBlank(value));
                                }
                                if (name.equalsIgnoreCase(layout)) {
                                    parser(buidDir, value.replace("@layout/", ""), briefness);
                                }
                            }
                            if (!view.getClassName().equalsIgnoreCase(include))
                                briefness.getLabel().addView(view);
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

    private static String id2String(String source) {
        String target = null;
        if (source.contains("@+id/")) {
            target = source.substring(source.indexOf("@+id/") + 5, source.length());
        }
        if (source.contains("@id/")) {
            target = source.substring(source.indexOf("@id/") + 4, source.length());
        }
        return target;
    }

    public static String deleteBlank(String str) {
        return str.replaceAll(" ", "");
    }
}
