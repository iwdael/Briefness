package com.hacknife.demo.adapter;

import android.view.View;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.adapter.base.BaseViewHolder;
import com.hacknife.demo.bean.Music;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@BindLayout(R.layout.item_music)
public class MusicViewHolder extends BaseViewHolder<MusicViewHolderBriefnessor, Music> {
    public MusicViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    protected void bindData(Music music) {
        briefnessor.setMusic(music);
    }
}
