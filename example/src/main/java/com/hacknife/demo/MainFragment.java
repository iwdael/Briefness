package com.hacknife.demo;

import android.app.Fragment;

import android.view.View;
import android.widget.TextView;

import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.BindView;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : BriefnessInjector
 */

@BindLayout(R.layout.activity_main)
public class MainFragment extends Fragment {
    @BindView(R.id.tv_test)
    TextView tv_test;
    @BindView(R.id.tv_test)
    TextView tv_test1;
    @BindView(R.id.tv_test)
    TextView tv_test2;
    @BindView(R.id.tv_test)
    TextView tv_test3;
    public Message message;


    public void onClick(View v) {
    }

    public void onTestClick(View v) {

    }

    public void OnClick(String username) {

    }
}
