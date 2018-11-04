package com.hacknife.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public abstract class BaseFragment<T extends Briefnessor> extends Fragment {

    T briefnessor;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(attachLayoutRes(), container, false);
        briefnessor = (T) Briefness.bind(this, view);
        return view;
    }

    protected abstract int attachLayoutRes();
}
