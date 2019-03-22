package com.hacknife.briefness.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hacknife on 2018/10/31.
 */

public class Label {
    List<Link> linkes;
    List<Link> viewModels;
    List<View> views;

    public Label() {
        linkes = new ArrayList<>();
        views = new ArrayList<>();
        viewModels = new ArrayList<>();
    }

    public void addViewModel(Link link){
        viewModels.add(link);
    }
    public void addLink(Link link) {
        linkes.add(link);
    }

    public void addView(View view) {
        views.add(view);
    }

    @Override
    public String toString() {
        return "Label{" +
                "linkes=" + linkes +
                ", views=" + views +
                '}';
    }

    public List<Link> getViewModels() {
        return viewModels;
    }

    public List<Link> getLinkes() {
        return linkes;
    }

    public List<View> getViews() {
        return views;
    }
}
