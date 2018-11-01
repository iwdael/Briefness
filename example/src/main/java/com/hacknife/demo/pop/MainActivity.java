package com.hacknife.demo.pop;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;


import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;
import com.hacknife.briefness.Briefness;
import com.hacknife.demo.BaseActivity;
import com.hacknife.demo.R2;


@BindLayout(R2.layout.activity_main)
public class MainActivity extends BaseActivity<MainActivityBriefnessor> {




    public void onUserClick(String s) {

    }

    public void onListener() {

    }

    public void onUserLongClick(String s) {

    }

    public void ontest() {

    }


    public interface CallBack{
        void  call();
    }
}
