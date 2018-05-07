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
    //briefness:imports Import the data types that need to be bound to the layout. Bind multiple types with separate. Separate data types and type aliases with commas.

    //briefness:bind Bind data, if it is a special control (such as custom View), you can also bind methods. If you need to bind more than one type of data, separate them with semicolons. Note: If you do not have a completion method, do not add a semicolon. You can also set only one type of data. Here are some common ways of writing.
    //briefness:bind="setText(entity.getUsername());setText(alisa.getUsername());"
    //briefness:bind="setText($entity.username$);setText($alisa.username$);"
    //briefness:bind="setText($entity.username$);"
    //briefness:bind="$entity.username$"

    //briefness:click Bind click events, ending with a semicolon, and you can also use variables in the bound class.
    //briefness:click="onClick(v);onTestClick(v);"
    //briefness:click="onClick(v);"
    //briefness:click="message.test();"


    //briefness:longclick Bind click events, ending with a semicolon, and you can also use variables in the bound class.
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
          compile 'com.github.blackchopper.briefness:briefness:v1.6.2'
    	  annotationProcessor 'com.github.blackchopper.briefness:briefness-compiler:v1.6.2'
 	}
 ```
<br><br><br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/blackchopper/CarouselBanner/blob/master/qq_group.png)
