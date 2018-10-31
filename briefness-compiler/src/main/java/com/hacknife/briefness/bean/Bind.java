package com.hacknife.briefness.bean;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class Bind {
    String bind;
    String alisa;
    String field;

    public Bind(String bind) {
        this.bind = bind;
        int start = bind.indexOf(".");
        int end = bind.lastIndexOf("$");
        this.alisa = bind.substring(1, start);
        this.field = bind.substring(start + 1, end);
    }

    public String getAlisa() {
        return alisa;
    }

    public String getField() {
        return field;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"bind\":\"")
                .append(bind).append('\"');
        sb.append(",\"alisa\":\"")
                .append(alisa).append('\"');
        sb.append(",\"field\":\"")
                .append(field).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
