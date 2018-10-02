package com.hacknife.briefness.databinding;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class XmlViewInfo {
    public String ID;
    public String click;
    public String longClick;
    public String touch;
    public String view;
    public String bind;
    public String bindSource;
    public String action;
    public String clazz;

    @Override
    public String toString() {
        return "XmlViewInfo{" +
                "ID='" + ID + '\'' +
                ", click='" + click + '\'' +
                ", longClick='" + longClick + '\'' +
                ", touch='" + touch + '\'' +
                ", view='" + view + '\'' +
                ", bind='" + bind + '\'' +
                ", action='" + action + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }
}
