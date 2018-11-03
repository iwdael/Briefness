package com.hacknife.demo.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.hacknife.demo.R;
import com.hacknife.demo.adapter.base.BaseAdapter;
import com.hacknife.demo.bean.Music;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class MusicAdapter extends BaseAdapter<MusicViewHolder, Music> {
    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicViewHolder(View.inflate(parent.getContext(), R.layout.item_music, null));
    }
}
