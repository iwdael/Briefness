package com.blackchopper.demo_briefness;


import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blackchopper.briefness.BindClick;
import com.blackchopper.briefness.BindLayout;
import com.blackchopper.briefness.BindView;
import com.blackchopper.briefness.Briefness;


@BindLayout("activity_main")
public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindClick(R.id.tv_test1)
    public void onClick() {
        Briefness.bind(briefnessor, new Entity("admin", "123"));
    }

    public void onChangeClick(String s, TextView tv_test1) {
        Log.v("Main", s);
        tv_test1.setText(s);
    }

    @Override
    public void onClick(View view) {

    }
}
