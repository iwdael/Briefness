package com.aliletter.demo_briefness;


import android.util.Log;
import android.widget.TextView;

import com.aliletter.briefness.BindLayout;
import com.aliletter.briefness.Briefness;


@BindLayout(id = R.layout.activity_main, name = "activity_main")
public class MainActivity extends BaseActivity {

    public void onClick() {
        Briefness.bind(briefnessor, new Entity("admin", "123"));

    }

    public void onChangeClick(String s, TextView tv_test1) {
        Log.v("Main", s);
        tv_test1.setText(s);
    }
}
