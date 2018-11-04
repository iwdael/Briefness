package com.hacknife.demo.mvvm.model;

import com.hacknife.demo.mvvm.base.IBaseModel;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public interface ILoginModel extends IBaseModel {
    void login(String account, String pswd);
}
