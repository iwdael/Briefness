package com.hacknife.demo.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public abstract class BaseDialog<T extends Briefnessor> extends Dialog {
    T briefnessor;
    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=View.inflate(getContext(),attachLayouRes(),null);
        setContentView(view);
        briefnessor= (T) Briefness.bind(this,view);
    }

    protected abstract int  attachLayouRes();
}
