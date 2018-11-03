package com.hacknife.demo.ui;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.bean.MulObj;
import com.hacknife.demo.bean.Multiple;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@BindLayout(R.layout.activity_multiple)
public class MultipleActivity extends BaseActivity<MultipleActivityBriefnessor> {
    @Override
    protected int attachTitleRes() {
        return R.string.title_multiple;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Multiple mul = new Multiple(1);
        briefnessor.setMul(mul);
        MulObj obj=new MulObj(4);
        briefnessor.setObj(obj);
    }
}
