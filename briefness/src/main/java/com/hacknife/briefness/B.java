package com.hacknife.briefness;

import android.view.LayoutInflater;
import android.view.View;

public class B<T> implements Briefnessor<T> {
    protected View view;
    protected T host;

    @Override
    public void bind(T target, Object source) {
        host = target;
    }

    public void bind(T target, Object source, int layout) {
        host = target;
        if (source == null) {
        } else if (layout == 0 && source instanceof View) {
            view = (View) source;
        } else if (layout !=0){
            if (source instanceof View)
                view = (View) source;
            else if (source instanceof LayoutInflater)
                view= ((LayoutInflater) source).inflate(layout,null);
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public void clearAll() {
        host = null;
    }

    @Override
    public void bindViewModel(Object viewModel) {

    }

    @Override
    public void notifyDataChange(Class clazz) {

    }

    @Override
    public View inflate() {
        return view;
    }
}
