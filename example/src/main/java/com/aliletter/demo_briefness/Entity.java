package com.aliletter.demo_briefness;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/30
 */

public class Entity {
    private String username;
    private String  name;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity(String username, String name) {
        this.username = username;
        this.name = name;
    }
}
