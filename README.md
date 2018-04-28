# Briefness  [![](https://jitpack.io/v/blackchopper/briefness.svg)](https://jitpack.io/#blackchopper/briefness)
Briefness makes mobile development easier, with support for data bindings, control bindings, layout bindings, and click event bindings.
## Instructions
Briefness can simplify development and remove some repetitive and boring jobs.[中文文档](https://github.com/blackchopper/Briefness/blob/master/README_CHINESE.md)
### Code Sample
You should initialize briefness before you used it in activity. If many activity requires briefness , you'd better initialize briefness in baseactivity.
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends Activity {
    //This class is automatically generated based on the bound class, named after the bound class name plus Briefnessor.
    private MainActivityBriefnessor briefnessor;

    @BindView(R.id.tv_test)
    TextView tv_test;
    @BindViews({R.id.tv_test, R.id.tv_test1})
    TextView[] textViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (MainActivityBriefnessor) Briefness.bind(this);
    }

    public void onClick(View v) {
    }

    public void onTestClick(View v) {
    }

    @BindClick(R.id.tv_test)
    public void click(View view) {
        briefnessor.setAlisa(new Entity());
        briefnessor.setEntity(new Entity());
    }

    @BindClick({R.id.tv_test, R.id.tv_test1})
    public void clicks(View view) {

    }
}
```
```Java
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:briefness="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    briefness:imports="com.blackchopper.demo_briefness.Entity,entity;com.blackchopper.demo_briefness.Entity,alisa;">
    //briefness:imports 导入需要和布局绑定的数据类型。绑定多个类型用分隔开。用逗号隔开数据类型和类型别名。

    //briefness:bind 绑定数据，如果是特殊控件(例如自定义View)，也可以把方法绑定上。如果需要绑定多个类型数据，需要用分号隔开。注意：如果没有补全方法，请不要添加分号，同时也只能设置一种数据。下面是几种常见的写法
    //briefness:bind="setText(entity.getUsername());setText(alisa.getUsername());"
    //briefness:bind="setText(@{entity.username});setText(@{alisa.username});"
    //briefness:bind="setText(@{entity.username});"
    //briefness:bind="@{entity.username}"

    //briefness:click 绑定点击事件,以分号结尾,同时也可以使用绑定的类中的变量。
    //briefness:click="onClick(v);onTestClick(v);"
    //briefness:click="onClick(v);"
    //briefness:click="message.test();"


    //briefness:longclick 绑定点击事件,以分号结尾,同时也可以使用绑定的类中的变量。
    //briefness:longclick="onClick(v);onTestClick(v);"
    //briefness:longclick="onClick(v);"
    //briefness:longclick="message.test();"
    <TextView
        android:id="@+id/tv_test"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World ONE"
        android:textSize="20sp"
        briefness:bind="setText(entity.getUsername());setText(alisa.getUsername());"
        briefness:click="message.test();"
        tools:ignore="MissingPrefix" />

    <TextView
        android:id="@+id/tv_test1"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World TWO"
        android:textSize="20sp"
        briefness:longclick="onClick(v);onTestClick(v);"
        briefness:bind="setText(entity.getUsername());setText(alisa.getUsername());"
        briefness:click="onClick(v);onTestClick(v);"
        tools:ignore="MissingPrefix" />

</LinearLayout>
```
## How to
To get a Git project into your build:
### Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories.   [click here for details](https://github.com/blackchopper/CarouselBanner/blob/master/root_build.gradle.png)
 ```Java
 	allprojects {
 		repositories {
 			...
 			maven { url 'https://jitpack.io' }
 		}
 	}
 ```
### Step 2. Add the dependency
Add it in your application module build.gradle at the end of dependencies where you want to use.[click here for details](https://github.com/blackchopper/CarouselBanner/blob/master/application_build.gradle.png)
 ```Java
 	dependencies {
          ...
          compile 'com.github.blackchopper.briefness:briefness:v1.5.9'
    	  annotationProcessor 'com.github.blackchopper.briefness:briefness-compiler:v1.5.9'
 	}
 ```
<br><br><br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/blackchopper/CarouselBanner/blob/master/qq_group.png)
