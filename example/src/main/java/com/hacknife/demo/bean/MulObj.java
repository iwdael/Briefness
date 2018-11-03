package com.hacknife.demo.bean;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class MulObj {
    int starLevel;

    public MulObj(int starLevel) {
        this.starLevel = starLevel;
    }

    public Object getStar() {
        return starLevel > 3 ? String.format("星%d", starLevel) : ViewState.GONE;
    }
}
