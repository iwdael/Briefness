package com.hacknife.demo.bean;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class TextColor {
    boolean status;
    int uncheck;
    int checked;

    public TextColor(boolean status, int checked, int uncheck) {
        this.status = status;
        this.uncheck = uncheck;
        this.checked = checked;
    }

    public int getColor() {
        return status ? checked : uncheck;
    }
}
