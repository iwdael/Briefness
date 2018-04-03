package com.aliletter.demo_briefness;


import android.view.View;

import com.aliletter.briefness.BindLayout;
import com.aliletter.briefness.Briefness;


@BindLayout(id = R.layout.activity_main, name = "activity_main")
public class MainActivity extends BaseActivity {

    public void onClick(View v) {
        Briefness.bind(briefnessor, new Entity("admin", "123"));
    }
}
