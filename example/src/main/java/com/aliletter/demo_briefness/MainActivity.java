package com.aliletter.demo_briefness;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aliletter.briefness.BindClass;
import com.aliletter.briefness.BindClick;
import com.aliletter.briefness.BindField;
import com.aliletter.briefness.BindLayout;
import com.aliletter.briefness.BindView;
import com.aliletter.briefness.Briefness;


@BindClass(clazz = "com.aliletter.demo_briefness.Entity", name = "name")
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @BindField(name = "name", field = "username",method = "setText")
    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindField(name = "name", field = "name")
    @BindView(R.id.tv_test1)

    TextView tv_test1;

    @BindClick({R.id.tv_test, R.id.tv_test1})
    public void onClick(View v) {

        if (v.getId() == R.id.tv_test) {
            Briefness.bind(this, new Entity("123", "321"));

            Toast.makeText(MainActivity.this, "TEST ONE", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MainActivity.this, "TEST TWO", Toast.LENGTH_SHORT).show();
        }
    }


}
