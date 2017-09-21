package com.absurd.demo_briefness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.absurd.briefness.BindClick;
import com.absurd.briefness.BindLayout;
import com.absurd.briefness.BindView;
import com.absurd.briefness.BindViews;

import java.util.List;

 public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @BindClick({R.id.tv_test, R.id.tv_test1})
    public void onClick(View v) {

        if (v.getId() == R.id.tv_test) {
            Toast.makeText(MainActivity.this, "TEST ONE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "TEST TWO", Toast.LENGTH_SHORT).show();
        }
    }


}
