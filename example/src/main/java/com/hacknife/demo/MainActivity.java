package com.hacknife.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;
import com.hacknife.briefness.Briefness;


@BindLayout(R.layout.activity_main)
public class MainActivity extends Activity {
    private MainActivityBriefnessor briefnessor;
    Message message;
    @BindView(R.id.tv_test)
    TextView tv_test;
    TextView[] textViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (MainActivityBriefnessor) Briefness.bind(this);
    }


    @BindClick(R.id.tv_test)
    public void click(View view) {

    }

    @BindClick({R.id.tv_test, R.id.tv_test1})
    public void clicks(View view) {

    }


    public void onUserClick(String s) {

    }

    public void onListener() {

    }

    public void onUserLongClick(String s) {

    }

    public void ontest() {

    }
}
