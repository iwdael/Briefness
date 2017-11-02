# Briefness  [![](https://jitpack.io/v/aliletter/briefness.svg)](https://jitpack.io/#aliletter/briefness)
Briefness is a android framework ,which keeps the code remarkably concise,and it likes butterknife. However , it supports binding layout .
# How to
To get a Git project into your build:
## Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
## Step 2. Add the dependency in your app build.gradle

	dependencies { 
          compile 'com.github.aliletter.briefness:briefness:v1.1.5'
    	  apt 'com.github.aliletter.briefness:briefness-compiler:v1.1.5'
	}
## Step 3. Add the classpath in your root build.gradle

	dependencies {
          classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
         
	}
##  Step 4. Add the plugin in your app build.gradle
	apply plugin: 'com.neenbedankt.android-apt'
# Instructions
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
or
```Java
@BindLayout(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    @BindViews({R.id.tv_test,R.id.tv_test1,R.id.tv_test2})
    TextView[] textViews;
    ...
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
## Fragment is also used in a similar way, in addition to binding layout .
There are a little different
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
 
