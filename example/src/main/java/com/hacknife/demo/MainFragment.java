package com.hacknife.demo;

import android.app.Fragment;

import android.view.View;
import android.widget.TextView;

import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public class MainFragment extends Fragment {

    @BindView({R.id.tv_test,R.id.tv_test,R.id.tv_test})
    TextView[] tv_test3;
    public Message message;


    @BindClick({R.id.tv_test,R.id.tv_test,R.id.tv_test})
    public void onClick(View v) {
    }

    public void onTestClick(View v) {

    }

    public void OnClick(String username) {

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
