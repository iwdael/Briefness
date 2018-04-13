package com.blackchopper.demo_briefness;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.blackchopper.briefness.BindLayout;
import com.blackchopper.briefness.BindView;
import com.blackchopper.briefness.BindViews;


@BindLayout(R.layout.activity_main)
public class MainActivity extends Activity {
    @BindViews({R.id.rc_view,R.id.rc_view,R.id.rc_view})
    RecyclerView[] rc_view;
    @BindView(R.id.rc_view)
    RecyclerView rc_tt;
    public void onClick(View v) {

    }

    public void onTestClick(View v) {

    }
}
