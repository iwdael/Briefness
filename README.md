# Briefness
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
  
## Step 2. Add the dependency

	dependencies {
          compile 'com.github.mr-absurd:briefness:v1.0.6'
          
	}
  

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
    @BindView(R.id.tv_test)
    List<TextView> textViews;
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
    List<TextView> list;
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
 
