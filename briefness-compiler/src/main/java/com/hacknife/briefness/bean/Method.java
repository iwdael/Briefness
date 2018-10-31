package com.hacknife.briefness.bean;

import java.util.Arrays;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class Method {
    String methodName;
    String[] ids;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public Method(String methodName, String[] ids) {
        this.methodName = methodName;
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "Method{" +
                "methodName='" + methodName + '\'' +
                ", ids=" + Arrays.toString(ids) +
                '}';
    }
}
