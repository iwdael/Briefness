package com.hacknife.briefness.bean;

import com.hacknife.briefness.util.ViewCollection;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class View {
    String className;
    String id;
    String click;
    String longClick;
    Bind bind;
    String fullClassName;
    Map<String, String> transfer;

    public View() {
        transfer = new HashMap<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        if (className.contains(".")) {
            fullClassName = className;
            this.className = className.substring(className.lastIndexOf(".") + 1);
        } else {
            this.className = className;
            fullClassName = ViewCollection.getFullNameByName(className);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bind getBind() {
        return bind;
    }

    public void setBind(String bind) {
        if (bind.trim().length() == 0) return;
        this.bind = new Bind(bind);
    }

    public String getFullClassName() {
        return fullClassName;
    }


    @Override
    public String toString() {
        return "View{" +
                "className='" + className + '\'' +
                ", id='" + id + '\'' +
                ", click='" + click + '\'' +
                ", longClick='" + longClick + '\'' +
                ", bind='" + bind + '\'' +
                ", fullClassName='" + fullClassName + '\'' +
                '}';
    }

    public void setTransfer(String name, String str) {
        transfer.put(name, str);
    }

    public Map<String, String> getTransfer() {
        return transfer;
    }
}
