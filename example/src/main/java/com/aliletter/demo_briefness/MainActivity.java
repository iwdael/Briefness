package com.aliletter.demo_briefness;

import android.view.View;
import android.widget.Toast;

import com.aliletter.briefness.BindClick;
import com.aliletter.briefness.BindLayout;


@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {



    @BindClick({R.id.tv_test, R.id.tv_test1})
    public void onClick(View v) {

        if (v.getId() == R.id.tv_test) {
            Toast.makeText(MainActivity.this, "TEST ONE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "TEST TWO", Toast.LENGTH_SHORT).show();
        }
    }


}
