package com.hacknife.demo.ui;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.bean.Live;

@BindLayout(R.layout.activity_live_data)
public class LiveDataActivity extends BaseActivity<LiveDataActivityBriefnessor> {

    Live live;

    @Override
    protected int attachTitleRes() {
        return R.string.title_live;
    }

    @Override
    protected void initView() {
        live = new Live("张三", "男", "1.7m");
        briefnessor.setLive(live);
    }

    public void onLiveClick() {
        live.setUsername("娜娜");
    }
}
