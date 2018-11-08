package com.hacknife.demo.bean;


import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class User {
    String username;
    int sex;
    int roleLevel;
      List<String> skill;

    public User(String username, int sex, int roleLevel, List<String> skill) {
        this.username = username;
        this.sex = sex;
        this.roleLevel = roleLevel;
        this.skill = skill;
    }

    public String getNickName() {
        return username;
    }

    public String getGender() {
        return sex == 1 ? "男" : "女";
    }

    public int getLevel() {
        return roleLevel;
    }

    public List<String> getSkill() {
        return skill;
    }

    public String getRank() {
        return String.format("等级%d" , roleLevel);
    }
}
