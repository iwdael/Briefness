package com.hacknife.briefness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class B<T> implements Briefnessor<T> {
    protected static int LAYOUT_NULL = 0;
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
        } else if (layout != 0) {
            if (source instanceof View)
                view = (View) source;
            else if (source instanceof LayoutInflater)
                view = ((LayoutInflater) source).inflate(layout, null);
            else if (source instanceof Context)
                view = LayoutInflater.from((Context) source).inflate(layout, null);
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
    public void bindViewModel(Object... viewModels) {
        for (Object viewModel : viewModels) {
            bindViewModels(viewModel);
        }
    }

    protected void bindViewModels(Object viewModel) {
    }

    @Override
    public void notifyDataChange(Class clazz) {

    }

    @Override
    public View inflate() {
        return view;
    }


    public <T> T setEntity(Object local, Object set) {
        if ((local == null || local != set) && (set instanceof ILiveData)) {
            ((ILiveData) (set)).bindTape(this);
        }
        if (local != null && local != set && local instanceof ILiveData) {
            ((ILiveData) local).bindTape(null);
        }
        return (T) set;
    }
}
