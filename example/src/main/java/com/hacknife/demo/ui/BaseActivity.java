package com.hacknife.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.hacknife.briefness.BindClick;
import com.hacknife.briefness.BindView;
import com.hacknife.briefness.Briefness;
import com.hacknife.briefness.Briefnessor;
import com.hacknife.demo.R;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
public abstract class BaseActivity<T extends Briefnessor> extends AppCompatActivity {
    T briefnessor;
    @BindView(R.id.toolBar_title)
    TextView toolBar_title;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (T) Briefness.bind(this);
        toolBar_title.setText(attachTitleRes());
    }


    @BindClick(R.id.toolBar_back)
    public void OnBackClick(View v) {
        finish();
    }
    protected abstract int attachTitleRes();

    protected void startActivity(Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
