package com.hacknife.briefness.bean;

import java.util.Arrays;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class Field {
    String className;
    String variable;
    String[] ids;
    String fullClass;
    String[] refrence;

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

    public Field(String className, String variable, String[] ids, String fullClass, String[] refrence) {
        this.className = className;
        this.variable = variable;
        this.ids = ids;
        this.fullClass = fullClass;
        this.refrence = refrence;
    }

    public String getFullClass() {
        return fullClass;
    }

    public String[] getRefrence() {
        return refrence;
    }

    @Override
    public String toString() {
        return "{" +
                "\"className\":\'" + className + "\'" +
                ", \"variable\":\'" + variable + "\'" +
                ", \"ids\":" + Arrays.toString(ids) +
                ", \"fullClass\":\'" + fullClass + "\'" +
                ", \"refrence\":" + Arrays.toString(refrence) +
                '}';
    }
}
