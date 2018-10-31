package com.hacknife.briefness.bean;

import java.util.Arrays;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class Field {
    String className;
    String variable;
    String[] ids;

    public String getClassName() {
        return className.replace("[", "").replace("]", "");
    }

    public String getClassType() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public Field(String className, String variable, String[] ids) {
        this.className = className;
        this.variable = variable;
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Field{" +
                "className='" + className + '\'' +
                ", variable='" + variable + '\'' +
                ", ids=" + Arrays.toString(ids) +
                '}';
    }
}
