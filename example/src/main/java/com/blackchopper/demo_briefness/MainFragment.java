package com.blackchopper.demo_briefness;

import android.widget.TextView;

import com.blackchopper.briefness.BindLayout;
import com.blackchopper.briefness.BindView;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

@BindLayout("activity_main")
public class MainFragment extends BaseFragment {
    @BindView(R.id.tv_test)
    TextView tv_test;
}
