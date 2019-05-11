package com.hacknife.demo.ui;

import android.widget.Toast;

import com.hacknife.briefness.BindField;
import com.hacknife.briefness.BindLayout;
import com.hacknife.briefness.Immersive;
import com.hacknife.demo.R;
import com.hacknife.demo.bean.PaCs;
import com.hacknife.demo.bean.User;
import com.hacknife.demo.constant.Transfer;


import java.util.ArrayList;
import java.util.List;

import static com.hacknife.demo.constant.Transfer.BUNDLE_NUMBER;

/**
 * author  : hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : briefness
 */
@Immersive
@BindLayout(R.layout.activity_normal)
public class NormalActivity extends BaseActivity<NormalActivityBriefnessor> {
    @Override
    protected int attachTitleRes() {
        return R.string.title_normal;
    }

    @BindField("short_1")
    short short_a;
    @BindField("short_2")
    Short short_b;
    @BindField("number_1")
    int number_a;
    @BindField("number_2")
    Integer number_b;

    @BindField("long_1")
    long long_a;
    @BindField("long_2")
    Long long_b;

    @BindField("double_1")
    double double_a;

    @BindField("double_2")
    Double double_b;

    @BindField("float_1")
    float float_a;

    @BindField("float_2")
    Float float_b;

    @BindField("char_1")
    char char_a;

    @BindField("boolean_1")
    boolean bool_a;


    @BindField("boolean_2")
    Boolean bool_b;

    @BindField("content_1")
    String content;

    @BindField("list_shoe")
    short[] shortList;

    @BindField("list_integer")
    int[] integerList;


    @BindField("list_long")
    long[] longList;

    @BindField("list_double")
    double[] doubleList;

    @BindField("list_float")
    float[] floatList;

    @BindField("list_bool")
    boolean[] booleans;

    @BindField("list_str")
    String[] stringList;

    @BindField("list_i")
    List<Integer> integers;

    @BindField("list_s")
    List<String> stringss;

    @BindField("asdasd")
    PaCs[] asds;

    @BindField("asdasd")
    List<PaCs> asds_asdasd;

    @BindField("aswwwwwwdasd")
    PaCs asdwqes_asdasd;

    @BindField(Transfer.BUNDLE_NUMBER)
    PaCs ad;

    @BindField(BUNDLE_NUMBER)
    Long cssd;

    public static final String TAG="tag";

    @BindField(TAG)
    char adsasdas;

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
