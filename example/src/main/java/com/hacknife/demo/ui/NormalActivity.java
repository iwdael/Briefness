package com.hacknife.demo.ui;

import android.widget.Toast;

import com.hacknife.briefness.BindLayout;
import com.hacknife.demo.R;
import com.hacknife.demo.bean.User;

import java.util.ArrayList;
import java.util.List;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@BindLayout(R.layout.activity_normal)
public class NormalActivity extends BaseActivity<NormalActivityBriefnessor> {
    @Override
    protected int attachTitleRes() {
        return R.string.title_normal;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<String> skill = new ArrayList<>();//一共有四个技能，但是我只添加三个，模拟项目中数据异常
        skill.add("water");
        skill.add("fire");
        skill.add("snow");
        User user = new User("张三", 1, 12, skill);
        User fiend = new User(null, 1, 24, null);

        briefnessor.setUser(fiend);//为了模仿数据异常,互换 user friend，同样不会出现空指针异常
        briefnessor.setFriend(user);
    }

    public void onComparisonClick(boolean b) {
        Toast.makeText(this, b ? "我的等级高" : "好友等级高", Toast.LENGTH_SHORT).show();
    }
}
