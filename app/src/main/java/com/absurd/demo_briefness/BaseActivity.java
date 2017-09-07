package com.absurd.demo_briefness;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.absurd.briefness.Briefness;


/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/8/16.
 */

public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Briefness.bind(this);
    }
}
