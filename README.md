# Briefness
[![](https://img.shields.io/badge/platform-android-orange.svg)](https://github.com/hacknife) [![](https://img.shields.io/badge/language-java-yellow.svg)](https://github.com/hacknife) [![](https://jitpack.io/v/com.hacknife/briefness.svg)](https://jitpack.io/#com.hacknife/briefness) [![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/hacknife)<br/><br/>
一个比DataBinding、XUtil、Butterknife 更加好用的框架，支持绑定数据、绑定View、绑定布局、绑定事件。
## 特点
* 支持任意数据类型绑定(包含自定义控件)
* 支持任意类绑定(Activity、Fragment、Dialog、View 如果有需要普通类也可以)
* 预防空指针引发的异常(防止项目开发中，数据异常引起的空指针)
* 支持View绑定(我不建议你这样做，因为根本没有必要)
* 支持点击事件双向绑定(Activity中绑、layout中也可以)
* 支持长按点击事件绑定
* 支持library、application
## 使用说明
Briefness 能够简化开发，去掉一些重复且枯燥的工作,比如：findViewById,setContentView,setOnClickListener等等.
### 代码示例
在你需要使用Briefness之前，先把它绑定到Activity，如果是多个Activity都需要使用，可以在基类中绑定。
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends Activity {
    //此类会根据绑定的类自动生成，以绑定的类名加上Briefnessor 命名。
    private MainActivityBriefnessor briefnessor;

    @BindView(R.id.tv_test)
    TextView tv_test;
    @BindViews({R.id.tv_test, R.id.tv_test1})
    TextView[] textViews;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //此方法必不可少
        briefnessor = (MainActivityBriefnessor) Briefness.bind(this);
    }
    //布局绑定的点击事件
    public void onClick(View v) {
    }

    //布局绑定的点击事件
    public void onTestClick(View v) {
    }

    //绑定单个View
    @BindClick(R.id.tv_test)
    public void click(View view) {
        briefnessor.setAlisa(new Entity());
        briefnessor.setEntity(new Entity());
    }
    //绑定多个View
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
    briefness:imports="com.hacknife.demo_briefness.Entity,entity;com.hacknife.demo_briefness.Entity,alisa;">
    //briefness:imports 导入需要和布局绑定的数据类型。绑定多个类型用分隔开。用逗号隔开数据类型和类型别名。

    //briefness:bind 绑定数据，如果是特殊控件(例如自定义View)，也可以把方法绑定上。如果需要绑定多个类型数据，需要用分号隔开。注意：如果没有补全方法，请不要添加分号，同时也只能设置一种数据。下面是几种常见的写法
    //briefness:bind="setText(entity.getUsername());setText(alisa.getUsername());"
    //briefness:bind="setText($entity.username$);setText($alisa.username$);"
    //briefness:bind="setText($entity.username$);"
    //briefness:bind="$entity.username$"

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
## 如何配置
将本仓库引入你的项目:
### Step 1. 添加JitPack仓库到Build文件
合并以下代码到项目根目录下的build.gradle文件的repositories尾。[点击查看详情](https://github.com/hacknife/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://hacknife.com' }
		}
	}
```
### Step 2. 添加依赖   
合并以下代码到需要使用的application Module的dependencies尾,注意替换version版本号。[点击查看详情](https://github.com/hacknife/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
                ...
          implementation 'com.hacknife.briefness:briefness:version'
    	  annotationProcessor 'com.hacknife.briefness:briefness-compiler:version'
	}
```
<br><br><br>
## 感谢浏览
如果你有任何疑问，请加入QQ群，我将竭诚为你解答。欢迎Star和Fork本仓库，当然也欢迎你关注我。
<br>
![Image Text](https://github.com/hacknife/CarouselBanner/blob/master/qq_group.png)
