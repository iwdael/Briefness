package com.blackchopper.demo_briefness;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


import com.blackchopper.briefness.BindClick;
import com.blackchopper.briefness.BindLayout;
import com.blackchopper.briefness.BindView;
import com.blackchopper.briefness.BindViews;
import com.blackchopper.briefness.Briefness;


@BindLayout(R.layout.activity_main)
public class MainActivity extends Activity {
    private MainActivityBriefnessor briefnessor;
    Message message;
    @BindView(R.id.tv_test)
    TextView tv_test;
    @BindViews({R.id.tv_test, R.id.tv_test1})
    TextView[] textViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (MainActivityBriefnessor) Briefness.bind(this);
    }

    public void onClick(View v) {
    }

    public void onTestClick(View v) {
    }

    @BindClick(R.id.tv_test)
    public void click(View view) {
//        briefnessor.setAlisa(new Entity());
//        briefnessor.setEntity(new Entity());
    }

    @BindClick({R.id.tv_test, R.id.tv_test1})
    public void clicks(View view) {

    }

    public void OnClick(String username) {

    }
}
