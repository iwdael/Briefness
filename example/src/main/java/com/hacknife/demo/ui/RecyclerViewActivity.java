package com.hacknife.demo.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.adapter.MusicAdapter;
import com.hacknife.demo.bean.Music;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@BindLayout(R.layout.activity_recyclerview)
public class RecyclerViewActivity extends BaseActivity<RecyclerViewActivityBriefnessor> {
    @Override
    protected int attachTitleRes() {
        return R.string.title_recyclerview;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MusicAdapter adapter = new MusicAdapter();
        List<Music> list = new ArrayList<>();
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        list.add(new Music("天下","张杰"));
        briefnessor.rc_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        briefnessor.rc_view.setAdapter(adapter);
        adapter.bindData(list);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
