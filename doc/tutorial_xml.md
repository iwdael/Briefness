### Xml属性
Briefness使用的属性标签Android控件并没有提供，需要在根布局中添加如下属性(tools:ignore="MissingPrefix")。
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:ignore="MissingPrefix">
</LinearLayout>
```
#### import
根布局中引入需要绑定的数据源，并设置别名，数据源与别名用英文“,”隔开。如果需要绑定多个数据源请采用“|”符号隔开。同时会在Briefnessor的实现类中生成
[set + 别名] 的方法，用于绑定数据。
```
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    app:imports="com.hacknife.demo.bean.Multiple,mul|com.hacknife.demo.bean.MulObj,obj"
    tools:ignore="MissingPrefix">
</LinearLayout>
```
自动生成
```
public class MultipleActivityBriefnessor implements Briefnessor<MultipleActivity> {

    ...
    public void setMul(Multiple mul) {
        if (mul == null) return;
    }

    public void setObj(MulObj obj) {
        if (obj == null) return;
    }
    ...
}
```
#### bind
需要绑定数据的控件必须添加android:id标签，数据源需要用成对的“$”符号包裹起来，如果有多个数据，需要用“|”隔开。
```
        <TextView
            android:id="@+id/tv_sex"
            style="@style/text_childer"
            app:bind="$mul.gender$|$mul.genderColor$" />
        <TextView
            android:id="@+id/tv_star"
            style="@style/text_childer"
            app:bind="$obj.star$" />
```
自动生成
```
public class MultipleActivityBriefnessor implements Briefnessor<MultipleActivity> {

    ...
    public void setMul(Multiple mul) {
        if (mul == null) return;
        BriefnessInjector.injector(tv_sex,mul.getGender());
        BriefnessInjector.injector(tv_sex,mul.getGenderColor());
    }

    public void setObj(MulObj obj) {
        if (obj == null) return;
        BriefnessInjector.injector(tv_star,obj.getStar());
    }
    ...
}
```
数据绑定会先通过BriefnessInjector.injector进入
```
public class BriefnessInjector {
    public static void injector(View view, Object value) {
        if (value == null | view == null) {
        } else if (ViewInjector.injector(view, value)) {
        } else if (view instanceof ImageView) {
            injectImageView((ImageView) view, value);
        } else if (view instanceof Button) {
            injectButton((Button) view, value);
        } else if (view instanceof EditText) {
            injectEditText((EditText) view, value);
        } else if (view instanceof TextView) {
            injectTextView((TextView) view, value);
        } else {
            Log.e("Briefness", "No match method and can not inject " + view.getClass().getSimpleName());
        }
    }
    ...
}
```
验证数据是否正常，然后进入之前提到的ViewInjector，该类在[包名.briefness.ViewInjector]，由你决定是否绑定数据，如果绑定[ture]则拦截，否则使用内置的BriefnessInjector。<br/>
在这里可以实现这自定义控件绑定数据，一些特殊数据源，比如：加载图片、字体颜色、背景色、是否显示等。
```
public class ViewInjector {
    public static boolean injector(View view, Object value) {
        if (view instanceof View && value instanceof ViewState) {
            view.setVisibility(value == ViewState.SHOW ? View.VISIBLE : View.GONE);
            return true;
        } else if (view instanceof TextView && value instanceof TextColor) {
            ((TextView) view).setTextColor(view.getResources().getColor(((TextColor) value).getColor()));
            return true;
        } else {
            return false;
        }
    }
}
```
#### click longClick
单击，长按事件的控件必须添加android:id标签。如果需要传出需要变换数据的数据源，请使用成对“$”符号，如果需要相应多个点击事件，请用“|”隔开。
```
    <Button
        android:id="@+id/btn_bind_data_error"
        app:click="onMultipleClick($btn_bind_data_error$)" />

    <Button
        android:id="@+id/btn_bind_recycler"
        app:click="onRecyclerViewClick" />
```
自动生成
```
public class DemoActivityBriefnessor implements Briefnessor<DemoActivity> {
    public Button btn_bind_data_error;
    public Button btn_bind_recycler;
    ...
    @Override
    public void bind(final DemoActivity host, Object source) {
        ...
        btn_bind_data_error = (Button) host.findViewById(R.id.btn_bind_data_error);
        btn_bind_recycler = (Button) host.findViewById(R.id.btn_bind_recycler);
        btn_bind_data_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.onMultipleClick(btn_bind_data_error.getText().toString().trim());
            }
        });
        btn_bind_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.onRecyclerViewClick();
            }
        });
    }
    ...
}
```
#### viewModel
待续

