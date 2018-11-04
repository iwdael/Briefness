package com.hacknife.demo.mvvm.viewmodel;

import android.widget.Toast;

import com.hacknife.demo.bean.LoginResult;
import com.hacknife.demo.mvvm.LoginActivityBriefnessor;
import com.hacknife.demo.mvvm.base.BaseViewModel;
import com.hacknife.demo.mvvm.model.ILoginModel;
import com.hacknife.demo.mvvm.model.LoginModel;
import com.hacknife.demo.mvvm.view.ILoginView;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class LoginViewModel extends BaseViewModel<ILoginView, ILoginModel, LoginActivityBriefnessor> implements ILoginViewModel {

    public LoginViewModel(ILoginView view, LoginActivityBriefnessor briefnessor) {
        super(view, briefnessor);
    }

    @Override
    protected ILoginModel createModel() {
        return new LoginModel(this);
    }

    @Override
    public void onLoginClick(String account, String pswd) {
        if (account.length() < 3) {
            Toast.makeText(context(), "账号不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pswd.length() < 3) {
            Toast.makeText(context(), "密码不正确", Toast.LENGTH_SHORT).show();
            return;
        }
        model.login(account, pswd);
    }

    @Override
    public void callbackLogin(LoginResult result) {
        briefnessor.setResult(result);
    }
}
