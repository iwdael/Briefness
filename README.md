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






}
    private String username;
    private String password;
    //This field is an alias field, when the Activity class, Entity objects need to bind different View collection, you need an alias field, if only a View collection is not required.
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
//Entity bound to MainActivity at the same time named entity. MainActivity can bind multiple JavaBeans. Clazz binding is Class JavaBean class name data, name is named data, the relationship between them is in accordance with the order of the corresponding.
@BindClass(clazz = {"com.blackchopper.demo_briefness.Entity"}, name = {"entity"})
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    //name = "entity" indicating that tv_test is bound to an object of com.blackchopper.demo_briefness.Entity named "entity".
    //field = "username" indicates that tv_test is bound to the username field in the JavaBean. At the same time tv_view can not be empty, so you need @BindView.
    //method = "setText" means that the data is bound using the setText method of tv_test, and the method of binding data common in Briefness has been implemented. Such as TextView, EditText, Button's setText method, and ImageView.setImageBitmap method.
    //If there are some special View, Briefness did not bind successfully, or need a special method binding, you can create a package named briefness class called BriefnessInjector at the same time achieve Injector interface, unbound successful View Will be Inject (View view, Onject obj) callback.
    //Such as ImageView binding String, the purpose is ImageView display pictures on the network, and Briefness no method to match, if the briefness.BriefnessInjector class exists, it will be exposed through this class, the binding process to achieve their own.
    //alias = "user" means that when the alias field of the Briefness.bind object must be user to be successfully bound to tv_view, here we only bind two View, at the same time the two View binding data are in the same Object, so you do not need alias alias.
    //So for the SDK comes with TextView, EditText, Button, ImageView and their subclasses do not need to specify the method.
    //@BindField can be reduced to the following ways.

    // @BindField(name = "entity", field = "username")
    @BindField(name = "entity", field = "username", method = "setText", alias = "user")
    @BindView(R.id.tv_test)
    TextView tv_test;

    @BindField(name = "entity", field = "password", method = "setText", alias = "user")
    @BindView(R.id.tv_test1)
    TextView tv_test1;
    ...
```
```Java
    //Binding data, some fields are empty nor null pointer exception occurs.
    Briefness.bind(this, new Entity("Tom", "admin123", "user"));
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
          compile 'com.github.blackchopper.briefness:briefness:v1.5.3'
    	  annotationProcessor 'com.github.blackchopper.briefness:briefness-compiler:v1.5.3'
 	}
 ```
<br><br><br>
## Thank you for your browsing
If you have any questions, please join the QQ group. I will do my best to answer it for you. Welcome to star and fork this repository, alse follow me.
<br>
![Image Text](https://github.com/blackchopper/CarouselBanner/blob/master/qq_group.png)
