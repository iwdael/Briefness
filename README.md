# Briefness  [![](https://jitpack.io/v/aliletter/briefness.svg)](https://jitpack.io/#aliletter/briefness)
Briefness is a android framework ,which keeps the code remarkably concise,and it likes butterknife. However , it supports binding layout . [[中文文档]](https://github.com/aliletter/Briefness/blob/master/README_CHINESE.md)
## Instructions
Briefness can simplify development and remove some repetitive and boring jobs.
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
package com.aliletter.demo_briefness
public class Entity {
    private String username;
    private String password;
    //此字段为别名字段，当Activity等类中，Entity的对象需要绑定不同的View集合时，需要别名字段，若只有一个View集合则不需要。
    private String alias;

    public Entity(String username, String password, String alias) {
        this.username = username;
        this.password = password;
        this.alias = alias;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAlias() {
        return alias;
    }
}
```
```Java
//将Entity绑定到MainActivity中，同时命名为entity。MainActivity可以绑定多个JavaBean。clazz绑定的是Class的JavaBean的全类名数据，name则是命名数据，他们之间的关系是按照顺序一一对应的。
@BindClass(clazz = {"com.aliletter.demo_briefness.Entity"}, name = {"entity"})
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    //name = "entity",表示tv_test与命名为"entity"的com.aliletter.demo_briefness.Entity的对象绑定。
    //field = "username"，表示tv_test与JavaBean中的username字段绑定。同时tv_view不能为空，所以有@BindView。
    //method = "setText",表示使用tv_test的setText方法绑定数据，这里省略不写，在Briefness中常见的绑定数据的方法已经实现。比如TextView、EditText、Button的setText的setText方法，以及ImageView.setImageBitmap方法。
    //如果有一些特殊的View，Briefness没有绑定成功，或者需要特殊方法绑定，则可以建立包名为com.aliletter.briefness，类名为BriefnessInjector的类，同时实现Injector接口，未绑定成功的View都会通过Inject(View view,Onject obj)回调。
    //alias = "user" 表示当Briefness.bind的对象的alias字段必须为user才能成功绑定到tv_view,这里我们只绑定了两个View，同时这两个View绑定所需的数据均在同一个对象中，所以不需要别名alias。
    //所以对于SDK自带的TextView、EditText、Button，ImageView以及他们的子类不需要指定method。
    //@BindField可以简化为以下方式。
    // @BindField(name = "entity", field = "username")
    @BindField(name = "entity", field = "username", method = "setText", alias = "user")
    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindField(name = "entity", field = "password", method = "setText", alias = "user")
    @BindView(R.id.tv_test1)
    TextView tv_test1;
    ...
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
Add it in your root build.gradle at the end of repositories.   [click here for details](https://github.com/aliletter/CarouselBanner/blob/master/root_build.gradle.png)
 ```Java
 	allprojects {
 		repositories {
 			...
 			maven { url 'https://jitpack.io' }
 		}
 	}
 ```
### Step 2. Add the dependency
Add it in your application module build.gradle at the end of dependencies where you want to use.[click here for details](https://github.com/aliletter/CarouselBanner/blob/master/application_build.gradle.png)
 ```Java
 	dependencies {
          ...
          compile 'com.github.aliletter.briefness:briefness:v1.1.8'
    	  annotationProcessor 'com.github.aliletter.briefness:briefness-compiler:v1.1.8'
 	}
 ```
<br><br><br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/aliletter/CarouselBanner/blob/master/qq_group.png)
