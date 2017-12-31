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
    private String alias;

    public Entity(String username, String password, String alias) {
        this.username = username;
        this.password = password;
        this.alias = alias;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAlias() {
        return alias;
    }
}
