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
    public static final String bind = ":bind";
    public static final String viewModel = ":viewModel";
    public static final String id = "android:id";
    /**
     * <attr name="textChange" format="string" />
     * <attr name="checkedChange" format="string" />
     * <attr name="tabSelected" format="string" />
     * <attr name="tabUnselected" format="string" />
     * <attr name="pageSelected" format="string" />
     */
    public static final String textChanged = ":textChanged";
    public static final String checkedChanged = ":checkedChanged";
    public static final String radioChanged = ":radioChanged";
    public static final String tabSelected = ":tabSelected";
    public static final String tabUnselected = ":tabUnselected";
    public static final String pageSelected = ":pageSelected";
    public static final String progressChanged = ":progressChanged";


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
                                /**
                                 * <attr name="textChange" format="string" />
                                 * <attr name="checkedChange" format="string" />
                                 * <attr name="tabSelected" format="string" />
                                 * <attr name="tabUnselected" format="string" />
                                 * <attr name="pageSelected" format="string" />
                                 */
                                if (name.endsWith(click) ||
                                        name.endsWith(longclick) ||
                                        name.endsWith(textChanged) ||
                                        name.endsWith(checkedChanged) ||
                                        name.endsWith(radioChanged) ||
                                        name.endsWith(tabSelected) ||
                                        name.endsWith(tabUnselected) ||
                                        name.endsWith(progressChanged) ||
                                        name.endsWith(pageSelected)
                                        ) {
                                    String str = deleteBlank(value);
                                    if (!checkLegality(str)) {
                                        error(
                                                processingEnv,
                                                "Briefness: " + name + " tag error in " + layoutName + ".xml",
                                                typeElement
                                        );
                                    } else {
                                        view.setTransfer(name, str);
                                    }

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
