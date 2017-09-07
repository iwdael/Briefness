package com.absurd.demo_briefness;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.absurd.briefness.base.BindClick;
import com.absurd.briefness.base.BindLayout;
import com.absurd.briefness.base.BindView;
import com.absurd.briefness.base.BindViews;

import java.util.List;

@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @BindViews({R.id.tv_test, R.id.tv_test1})
    List<TextView> textViews;
    @BindView(R.id.tv_test)
    TextView view1;
    @BindView(R.id.tv_test1)
    TextView view2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        textViews.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "TEST ONE", Toast.LENGTH_SHORT).show();
            }
        });
        textViews.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "TEST ONE", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    @BindClick({R.id.tv_test, R.id.tv_test1})
//    public void onClick(View v) {
//
//        if (v.getId() == R.id.tv_test) {
//            Toast.makeText(MainActivity.this, "TEST ONE", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MainActivity.this, "TEST TWO", Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
          Log.v("TAG", "------------------>>" + view1.hashCode());
    }
}
