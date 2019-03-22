package com.hacknife.demo.ui;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.mvvm.LoginActivity;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@BindLayout(R.layout.activity_demo)
public class DemoActivity extends BaseActivity<DemoActivityBriefnessor> {

    @Override
    protected int attachTitleRes() {
        return R.string.title_demo;
    }

    public void onRecyclerViewClick() {
        startActivity(RecyclerViewActivity.class);
    }

    public void onNormalClick() {
        startActivity(NormalActivity.class);
    }

    public void onMultipleClick() {

        startActivity(MultipleActivity.class);
    }

    public void onMVVMClick() {
        startActivity(LoginActivity.class);
    }

    public void onLiveDataClick() {
        startActivity(LiveDataActivity.class);
    }
}
