package com.hacknife.briefness.bean;

import com.hacknife.briefness.util.StringUtil;

import java.util.Arrays;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class Bind {
    String bind;
    String[] method;
    String[] protect;
    String[] alisa;
    boolean inited;

    public Bind(String bind) {
        this.bind = bind;
        inited = false;

    }

    public String[] getMethod(List<Link> links) {
        init(links);
        return method;
    }

    public String[] getProtect(List<Link> links) {
        init(links);
        return protect;
    }

    public String[] getAlisa(List<Link> links) {
        init(links);
        return alisa;
    }

    private void init(List<Link> links) {
        if (inited) return;
        String[] variables;
        if (bind == null || bind.replaceAll(" ", "").length() == 0)
            variables = new String[0];
        else
            variables = bind.split(";");
        method = new String[variables.length];
        protect = new String[variables.length];
        alisa = new String[variables.length];
        for (int i = 0; i < variables.length; i++) {
            String variable = variables[i];
            StringBuilder builder = new StringBuilder();
            method[i] = StringUtil.variable2Method(variable, links, builder);
            protect[i] = builder.toString();
            int start = variable.indexOf("$") + 1;
            int end = variable.indexOf("$", start);
            String params = variable.substring(start, end);
            int index = params.indexOf(".");
            alisa[i] = params.substring(0, index);
        }
        inited = true;
    }

    @Override
    public String toString() {
        return "Bind{" +
                "bind='" + bind + '\'' +
                ", method=" + Arrays.toString(method) +
                ", protect=" + Arrays.toString(protect) +
                ", alisa=" + Arrays.toString(alisa) +
                ", inited=" + inited +
                '}';
    }
}
