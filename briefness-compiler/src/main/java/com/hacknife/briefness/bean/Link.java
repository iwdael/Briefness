package com.hacknife.briefness.bean;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class Link {
    String fullClassName;
    String className;
    String alisa;

    public Link(String fullClassName, String alisa) {
        this.fullClassName = fullClassName;
        this.alisa = alisa;
        this.className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
    }

    @Override
    public String toString() {
        return "Link{" +
                "fullClassName='" + fullClassName + '\'' +
                ", alisa='" + alisa + '\'' +
                '}';
    }

    public String getAlisa() {
        return alisa;
    }

    public String getFullClassName() {
        return fullClassName;
    }

    public String getClassName() {
        return className;
    }
}
