# Briefness  [![](https://jitpack.io/v/blackchopper/briefness.svg)](https://jitpack.io/#blackchopper/briefness)
Briefness让移动开发更加简单，它支持数据绑定，控件绑定，布局绑定，点击事件绑定。
## 使用说明
Briefness 能够简化开发，去掉一些重复且枯燥的工作,比如：findViewById,setContentView,setOnClickListener等等。
### 代码示例
在你需要使用Briefness之前，先把它绑定到Activity，如果是多个Activity都需要使用，可以在基类中绑定。
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
@BindLayout可以代替setContentView(int)方法，在一些特定的场景更加方便。
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
...

}

```
@BindLayout可以代替findViewById(int)，是代码更加简洁。
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
@BindField和@BindClass组合使用实现绑定数据，目前只支持JavaBean对象绑定，后面会支持更多的数据类型。@BindClass将需要绑定的数据类型绑定到当前类，@BindField将View绑定到JavaBean中的字段。通过Briefness.bind(this,JavaBean)方法绑定数据。注意：JavaBean中需要绑定的字段都必须有标准的get方法，同时View对象不能为空，建议和@BindView组合使用。
<br>若同一类型，需要绑定不同的View集合，则可以采用别名的方式，同样，JavaBean中必须有alias的String字段，并有getAlias()方法。
<br>若绑定的View为自定义控件，则需要设置绑定方法。
```Java
package com.blackchopper.demo_briefness
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
@BindClass(clazz = {"com.blackchopper.demo_briefness.Entity"}, name = {"entity"})
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    //name = "entity",表示tv_test与命名为"entity"的com.blackchopper.demo_briefness.Entity的对象绑定。
    //field = "username"，表示tv_test与JavaBean中的username字段绑定。同时tv_view不能为空，所以需要@BindView。
    //method = "setText",表示使用tv_test的setText方法绑定数据，这里省略不写，在Briefness中常见的绑定数据的方法已经实现。比如TextView、EditText、Button的setText方法，以及ImageView.setImageBitmap方法。
    //如果有一些特殊的View，Briefness没有绑定成功，或者需要特殊方法绑定，则可以建立包名为com.blackchopper.briefness，类名为BriefnessInjector的类，同时实现Injector接口，未绑定成功的View都会通过Inject(View view,Onject obj)回调。
    //比如ImageView绑定String，其目的是ImageView展示网络上的图片，而Briefness没有方法可以匹配，如果com.blackchopper.briefness.BriefnessInjector类存在，则会通过这个类暴露出来，自行实现绑定过程。
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
```Java
    //绑定数据，某些字段为空也不会出现空指针异常。
    Briefness.bind(this, new Entity("Tom", "admin123", "user"));
```

@BindClick可以代替setOnClickListener(View.OnClickListener)，并且它支持绑定单个或多个Id。
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
在Fragment使用也是类似的，只有绑定的时候有区别。Briefness.bind(this, view)。this可以为fragment,也可以是Activity，甚至是一些类都可以，但是参数view对象必须View类的子类。
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
## 如何配置
将本仓库引入你的项目:
### Step 1. 添加JitPack仓库到Build文件
合并以下代码到项目根目录下的build.gradle文件的repositories尾。[点击查看详情](https://github.com/blackchopper/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. 添加依赖   
合并以下代码到需要使用的application Module的dependencies尾。[点击查看详情](https://github.com/blackchopper/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
                ...
          compile 'com.github.blackchopper.briefness:briefness:v1.3.1'
    	  annotationProcessor 'com.github.blackchopper.briefness:briefness-compiler:v1.3.1'
	}
```
<br><br><br>
## 感谢浏览
如果你有任何疑问，请加入QQ群，我将竭诚为你解答。欢迎Star和Fork本仓库，当然也欢迎你关注我。
<br>
![Image Text](https://github.com/blackchopper/CarouselBanner/blob/master/qq_group.png)
