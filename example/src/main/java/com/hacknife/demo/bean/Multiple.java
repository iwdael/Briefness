package com.hacknife.demo.bean;

import com.hacknife.demo.R;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class Multiple {
    int sex;

    public Multiple(int sex) {
        this.sex = sex;
    }

    public String getGender() {
        return sex == 1 ? "男" : "女";
    }

    public TextColor getGenderColor() {
        return new TextColor(sex == 1, R.color.blue, R.color.pink);
    }
}
