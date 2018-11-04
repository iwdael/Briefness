package com.hacknife.briefness;

import com.hacknife.briefness.bean.Briefness;
import com.hacknife.briefness.bean.Link;
import com.hacknife.briefness.bean.View;
import com.hacknife.briefness.util.Logger;
import com.hacknife.briefness.util.StringUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class XmlParser {
    public static final String imports = ":imports";
    public static final String click = ":click";
    public static final String longclick = ":longClick";
    public static final String touch = ":touch";
    public static final String bind = ":bind";
    public static final String action = ":action";
    public static final String viewModel = ":viewModel";
    public static final String transfer = ":transfer";
    public static final String longTransfer = ":longTransfer";
    public static final String id = "android:id";

    public static final String SPLIT = "/";
    public static final String include = "include";
    public static final String layout = "layout";


    public static void parser(String buidDir, String layoutName, Briefness briefness, ProcessingEnvironment processingEnv, TypeElement typeElement) {
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
                        boolean validView = false;
                        String includeLayout = null;
                        int count = parser.getAttributeCount();
                        //获取引用
                        for (int i = 0; i < count; i++) {
                            if (parser.getAttributeName(i).endsWith(viewModel)) {
                                if (parser.getAttributeValue(i).contains(";") || parser.getAttributeValue(i).contains("|")) {
                                    error(
                                            processingEnv,
                                            "Briefness: viewModel tag error in " + layoutName + ".xml",
                                            typeElement
                                    );
                                }
                                briefness.getLabel().addLink(new Link(parser.getAttributeValue(i).replaceAll(" ", ""), "viewModel"));
                            }
                            if (parser.getAttributeName(i).endsWith(imports)) {
                                String[] links;
                                if (parser.getAttributeValue(i).contains("|")) {
                                    links = parser.getAttributeValue(i).replaceAll(" ", "").split("\\|");
                                } else {
                                    links = parser.getAttributeValue(i).replaceAll(" ", "").split(";");
                                }
                                for (String link : links) {
                                    if (link.contains("|") || link.contains(";") || (!link.contains(",")))
                                        error(
                                                processingEnv,
                                                "Briefness: imports tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                }
                                for (String link : links) {
                                    String[] split = link.split(",");
                                    Link aLink = new Link(split[0], split[1]);
                                    briefness.getLabel().addLink(aLink);
                                }
                            }
                            if (parser.getAttributeName(i).contains(id))
                                validView = true;
                            if (parser.getName().contains(include) && parser.getAttributeName(i).equalsIgnoreCase(layout)) {
                                includeLayout = parser.getAttributeValue(i).replace("@layout/", "");
                            }
                        }
                        if (includeLayout != null) {
                            parser(buidDir, includeLayout, briefness, processingEnv, typeElement);
                            validView = false;
                        }
                        //获取信息
                        if (validView) {
                            View view = new View();
                            view.setClassName(parser.getName());
                            for (int i = 0; i < count; i++) {
                                String name = parser.getAttributeName(i);
                                String value = parser.getAttributeValue(i);
                                if (name.equalsIgnoreCase(id)) {
                                    view.setId(id2String(value));
                                }
                                if (name.endsWith(click)) {
                                    String str = deleteBlank(value);
                                    if (!checkLegality(str)) {
                                        error(
                                                processingEnv,
                                                "Briefness: click tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                    } else {
                                        view.setClick(str);
                                    }

                                }
                                if (name.endsWith(longclick)) {
                                    String str = deleteBlank(value);
                                    if (!checkLegality(str)) {
                                        error(
                                                processingEnv,
                                                "Briefness: longClick tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                    } else {
                                        view.setLongClick(str);
                                    }
                                }
                                if (name.endsWith(touch)) {
                                    view.setTouch(deleteBlank(value));
                                }
                                if (name.endsWith(bind)) {
                                    String str = deleteBlank(value);
                                    if (!checkLegality(str)) {
                                        error(
                                                processingEnv,
                                                "Briefness: bind tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                    } else {
                                        view.setBind(str);
                                    }
                                }
                                if (name.endsWith(action)) {
                                    view.setAction(deleteBlank(value));
                                }
                                if (name.endsWith(transfer)) {
                                    String str = deleteBlank(value);
                                    if (!checkLegality(str)) {
                                        error(
                                                processingEnv,
                                                "Briefness: transfer tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                    } else {
                                        view.setTransfer(str);
                                    }
                                }
                                if (name.endsWith(longTransfer)) {
                                    String str = deleteBlank(value);
                                    if (!checkLegality(str)) {
                                        error(
                                                processingEnv,
                                                "Briefness: longTransfer tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                    } else {
                                        view.setLongTransfer(str);
                                    }
                                }
                            }
                            briefness.getLabel().addView(view);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = parser.next();

            }
        } catch (Exception e) {
            Logger.v(e.getMessage());
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


    private static void error(ProcessingEnvironment environment, String msg, TypeElement element) {
        environment.getMessager().printMessage(Diagnostic.Kind.ERROR, msg, element);
    }

    public static void main(String[] args) {
        String str = "jhakhgakgr$dfj$flj;f$";
        System.out.print(StringUtil.charCount(str, "$"));
    }

    private static boolean checkLegality(String string) {
        String[] split;
        if (string.contains("|")) {
            split = string.split("\\|");
        } else {
            split = string.split(";");
        }
        for (String s : split) {
            if (StringUtil.charCount(s, "$") % 2 != 0)
                return false;
        }
        return true;
    }

}
