package com.hacknife.demo.mvvm.viewmodel;

import com.hacknife.demo.bean.LoginResult;
import com.hacknife.demo.mvvm.base.IBaseViewModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public interface ILoginViewModel extends IBaseViewModel {
    void onLoginClick(String account, String pswd);

    void callbackLogin(LoginResult result);

}
