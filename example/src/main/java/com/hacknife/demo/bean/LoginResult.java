package com.hacknife.demo.bean;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public class LoginResult {
    int code;
    String msg;

    public LoginResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public String getMsg() {
        return msg;
    }
}
