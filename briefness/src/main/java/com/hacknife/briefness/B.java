package com.hacknife.briefness;

import android.view.View;

public class B<T> implements Briefnessor<T> {
    protected View view;
    protected T host;

    @Override
    public void bind(T target, Object source) {
        host = target;
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
