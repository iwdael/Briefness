package com.hacknife.demo.bean;

import com.hacknife.briefness.LiveData;

public class Live extends LiveData {
    String username;
    String sex;
    String height;

    public Live(String username, String sex, String height) {
        this.username = username;
        this.sex = sex;
        this.height = height;
    }

    public void setSex(String sex) {
        this.sex = sex;
        notifyDataChange();
    }

    public void setUsername(String username) {
        this.username = username;
        notifyDataChange();
    }

    public void setHeight(String height) {
        this.height = height;
        notifyDataChange();
    }

    public String getUsername() {
        return username;
    }

    public String getSex() {
        return sex;
    }

    public String getHeight() {
        return height;
    }
}
