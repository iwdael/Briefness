package com.hacknife.briefness.bean;

import com.hacknife.briefness.util.ViewCollection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class View {
    String className;
    String id;
    String click;
    String longClick;
    String touch;
    List<Bind> bind;
    String action;
    String fullClassName;

    public View() {
        bind = new ArrayList<>();
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {

        if (className.contains(".")) {
            this.className = className.substring(className.indexOf(".") + 1);
            fullClassName = className;
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

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click.replaceAll(" ", "");
    }

    public String getLongClick() {
        return longClick;
    }

    public void setLongClick(String longClick) {
        this.longClick = longClick.replaceAll(" ", "");
    }

    public String getTouch() {
        return touch;
    }

    public void setTouch(String touch) {
        this.touch = touch;
    }

    public List<Bind> getBind() {
        return bind;
    }

    public void setBind(String bind) {
        if (bind.trim().length() == 0) return;
        String[] vars = bind.split(";");
        for (String var : vars) {
            this.bind.add(new Bind(var));
        }
    }



    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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
                ", touch='" + touch + '\'' +
                ", bind='" + bind + '\'' +
                ", action='" + action + '\'' +
                ", fullClassName='" + fullClassName + '\'' +
                '}';
    }
}
