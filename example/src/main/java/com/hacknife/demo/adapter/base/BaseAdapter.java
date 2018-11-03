package com.hacknife.demo.adapter.base;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public abstract class BaseAdapter<T extends BaseViewHolder, E> extends RecyclerView.Adapter<T> {
    List<E> list;

    public BaseAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.bindData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void bindData(List<E> data) {
        list.clear();
        list.addAll(data
        );
        notifyDataSetChanged();
    }
}
