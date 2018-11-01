package com.hacknife.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;

/**
 * Created by Hacknife on 2018/11/1.
 */

public class BaseActivity<T extends Briefnessor> extends Activity {
    T briefnessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (T) Briefness.bind(this);
    }
}
