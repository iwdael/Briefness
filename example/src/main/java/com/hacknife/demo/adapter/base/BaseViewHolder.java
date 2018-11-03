package com.hacknife.demo.adapter.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public abstract class BaseViewHolder<T extends Briefnessor, E> extends RecyclerView.ViewHolder {
    protected T briefnessor;

    public BaseViewHolder(View itemView) {
        super(itemView);
        briefnessor = (T) Briefness.bind(this, itemView);
    }

    protected abstract void bindData(E e);
}
