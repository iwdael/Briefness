# Briefness  [![](https://jitpack.io/v/aliletter/briefness.svg)](https://jitpack.io/#aliletter/briefness)
Briefness是一个安卓框架，它使代码非常简洁，并支持结合布局，控件，单击。
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
合并以下代码到项目根目录下的build.gradle文件的repositories尾。[点击查看详情](https://github.com/aliletter/CarouselBanner/blob/master/root_build.gradle.png)
```Java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
### Step 2. 添加依赖   
合并以下代码到需要使用的application Module的dependencies尾。[点击查看详情](https://github.com/aliletter/CarouselBanner/blob/master/application_build.gradle.png)
```Java
	dependencies {
                ...
          compile 'com.github.aliletter.briefness:briefness:v1.1.5'
    	  apt 'com.github.aliletter.briefness:briefness-compiler:v1.1.5'
	}
```
<br><br><br>
## 感谢浏览
如果你有任何疑问，请加入QQ群，我将竭诚为你解答。欢迎Star和Fork本仓库，当然也欢迎你关注我。
<br>
![Image Text](https://github.com/aliletter/CarouselBanner/blob/master/qq_group.png)