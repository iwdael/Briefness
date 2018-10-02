package com.hacknife.briefness.databinding;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class XmlBind {
    public String clazz;
    public String name;
    public String alisa;
    public List<XmlViewInfo> list;

    public XmlBind(String clazz, String name, String alisa) {
        this.clazz = clazz;
        this.name = name;
        this.alisa = alisa;
        list = new ArrayList<>();
    }
}
