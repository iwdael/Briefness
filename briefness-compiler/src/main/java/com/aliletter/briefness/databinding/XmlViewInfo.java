package com.aliletter.briefness.databinding;

/**
 * e-mail : aliletter@qq.com
 * time   : 2018/04/02
 * desc   :
 * version: 1.0
 */
public class XmlViewInfo {
    public String ID;
    public String click;
    public String longClick;
    public String touch;
    public String view;
    public String bind;
    public String clazz;

    @Override
    public String toString() {
        return "XmlViewInfo{" +
                "ID='" + ID + '\'' +
                ", click='" + click + '\'' +
                ", longClick='" + longClick + '\'' +
                ", action='" + touch + '\'' +
                ", view='" + view + '\'' +
                ", bind='" + bind + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }
}
