package com.hacknife.demo;

import java.util.List;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : BriefnessInjector
 */

public class Entity {
    private String username;
    private String password;
    private int index;
    private List<String> list;

    public List<String> getList() {
        return list;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
