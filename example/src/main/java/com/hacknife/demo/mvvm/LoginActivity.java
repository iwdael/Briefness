package com.hacknife.demo.mvvm;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.mvvm.base.BaseActivity;
import com.hacknife.demo.mvvm.view.ILoginView;
import com.hacknife.demo.mvvm.viewmodel.ILoginViewModel;
import com.hacknife.demo.mvvm.viewmodel.LoginViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@BindLayout(R.layout.activity_login)
public class LoginActivity extends BaseActivity<ILoginViewModel, LoginActivityBriefnessor> implements ILoginView {
    @Override
    protected ILoginViewModel createViewModel(LoginActivityBriefnessor briefnessor) {
        return new LoginViewModel(this, briefnessor);
    }
}
