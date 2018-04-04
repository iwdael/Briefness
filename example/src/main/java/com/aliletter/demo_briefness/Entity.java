package com.aliletter.demo_briefness;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/30
 */

public class Entity {
    private String username;
    private String password;


    public Entity(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
