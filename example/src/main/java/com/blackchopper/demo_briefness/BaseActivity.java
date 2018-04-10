package com.blackchopper.demo_briefness;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blackchopper.briefness.Briefness;
import com.blackchopper.briefness.Briefnessor;


/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

public class BaseActivity extends AppCompatActivity {
    protected Briefnessor briefnessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = Briefness.bind(this);

    }
}
