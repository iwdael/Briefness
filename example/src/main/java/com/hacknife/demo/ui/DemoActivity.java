package com.hacknife.demo.ui;

import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

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

    public void onMultipleClick(String trim) {
        Toast.makeText(DemoActivity.this, trim, Toast.LENGTH_SHORT).show();
        startActivity(MultipleActivity.class);
    }

    public void onMVVMClick() {
        startActivity(LoginActivity.class);
    }
}
