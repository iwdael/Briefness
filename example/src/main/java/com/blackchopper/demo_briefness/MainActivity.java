package com.blackchopper.demo_briefness;


import android.util.Log;
import android.widget.TextView;

import com.blackchopper.briefness.BindLayout;
import com.blackchopper.briefness.Briefness;


@BindLayout("activity_main")
public class MainActivity extends BaseActivity {

    public void onClick() {
        Briefness.bind(briefnessor, new Entity("admin", "123"));
    }

    public void onChangeClick(String s, TextView tv_test1) {
        Log.v("Main", s);
        tv_test1.setText(s);
    }
}
