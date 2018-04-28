# Briefness  [![](https://jitpack.io/v/blackchopper/briefness.svg)](https://jitpack.io/#blackchopper/briefness)
Briefness makes mobile development easier, with support for data bindings, control bindings, layout bindings, and click event bindings.
## Instructions
Briefness can simplify development and remove some repetitive and boring jobs.[中文文档](https://github.com/blackchopper/Briefness/blob/master/README_CHINESE.md)
### Code Sample
You should initialize briefness before you used it in activity. If many activity requires briefness , you'd better initialize briefness in baseactivity.
```Java
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Briefness.bind(this);
        ...
    }
    ...
}
```
Annotate class that extend Activity , and you don'd need to realize the method -- setContentView(int)
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
...

}

```
Annotate Field that extend View , and you don'd need to realize the method -- findViewById(int)
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_test)
    TextView textView;
    ...
}
```
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindViews({R.id.tv_test,R.id.tv_test1,R.id.tv_test2})
    TextView[] textViews;
    ...
}
```
@BindField and @BindClass combination of the use of binding data, currently only supports JavaBean object binding, the latter will support more data types. @BindClass Binds the data types that need to be bound to the current class @ BindField binds the View to the fields in the JavaBean. Bind data with the Briefness.bind (this, JavaBean) method. Note: JavaBean need to bind the field must have a standard get method, while the View object can not be empty, it is recommended and @ BindView used in combination.
<br>If the same type, you need to bind a different View collection, you can use the alias way, the same JavaBean alias String field must have getAlias () method.
<br>If the bound View is a custom control, you need to set the binding method.

```Java
package com.blackchopper.demo_briefness
public class Entity {
    private String username;
    private String password;

    public Entity(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

```
Annotate Method that the parameter is View , and you don'd need to realize the method -- setOnClickListener(View.OnClickListener)
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_test)
    TextView textView;
    ...
    @BindClick({R.id.tv_test, R.id.tv_test1})//parameter is a array
    public void onClick(View view) {
    .....
    }
}

```
Fragment is also used in a similar way, in addition to binding layout. There are a little different.
```Java
public abstract class BaseFragment extends Fragment {

    @BindViews({R.id.tv_test, R.id.tv_test1})
    TextView[] list;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(onAttachLaoutRes(), null);
	...
        Briefness.bind(this, view);
   	...
        return view;
    }
}
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
          compile 'com.github.blackchopper.briefness:briefness:v1.5.6'
    	  annotationProcessor 'com.github.blackchopper.briefness:briefness-compiler:v1.5.6'
 	}
 ```
<br><br><br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/blackchopper/CarouselBanner/blob/master/qq_group.png)
