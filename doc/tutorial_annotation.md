### 功能入口
Briefness.bind方法为功能入口，该方法会返回Briefenssor接口的一个实现类对象，该方法必须在使用Briefness提供的功能之前调用。
建议该方法基类中使用。避免子类重复操作。<br/>
Activity
```
public abstract class BaseActivity<T extends Briefnessor> extends AppCompatActivity {

    T briefnessor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (T) Briefness.bind(this);
     }
}
```
Fragment
```
public abstract class BaseFragment<T extends Briefnessor> extends Fragment {

    T briefnessor;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(attachLayoutRes(), container, false);
        briefnessor = (T) Briefness.bind(this, view);
        return view;
    }

    protected abstract int attachLayoutRes();
}
```
Dialog
```
public abstract class BaseDialog<T extends Briefnessor> extends Dialog {
    T briefnessor;
    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view=View.inflate(getContext(),attachLayouRes(),null);
        setContentView(view);
        briefnessor= (T) Briefness.bind(this,view);
    }

    protected abstract int  attachLayouRes();
}

```
其它类的绑定方式，往后看你就知道。
### 注解
BindLayout、BindView、BindClick均可以根据需求任意类中使用，make project [🔨]，会自动生成该 “类名+Briefnessor” 的类，该类提供了所有你需要的功能。
#### BindLayout
功能为绑定布局（不用setContentView），同时解析布局文件。***注意：该方法绑定布局功能针对Activity类***
```
@BindLayout(R.layout.activity_demo)
public class DemoActivity extends BaseActivity<DemoActivityBriefnessor> {

    @Override
    protected int attachTitleRes() {
        return R.string.title_demo;
    }

    public void onRecyclerViewClick() {
        startActivity(RecyclerViewActivity.class);
    }

    public void onNormalClick() {
        startActivity(NormalActivity.class);
    }

    public void onMultipleClick(String trim) {
        Toast.makeText(DemoActivity.this, trim, Toast.LENGTH_SHORT).show();
        startActivity(MultipleActivity.class);
    }
}
```
该类为自动生成，解析了整个布局文件
```
public class DemoActivityBriefnessor implements Briefnessor<DemoActivity> {
    public ImageView toolBar_back;
    public TextView toolBar_title;
    public Button btn_bind_normal;
    public Button btn_bind_data_error;
    public Button btn_bind_recycler;

    @Override
    public void bind(final DemoActivity host, Object source) {
        if (!Utils.contentViewExist(host)) {
            host.setContentView(R.layout.activity_demo);
        }
        toolBar_back = (ImageView) host.findViewById(R.id.toolBar_back);
        toolBar_title = (TextView) host.findViewById(R.id.toolBar_title);
        btn_bind_normal = (Button) host.findViewById(R.id.btn_bind_normal);
        btn_bind_data_error = (Button) host.findViewById(R.id.btn_bind_data_error);
        btn_bind_recycler = (Button) host.findViewById(R.id.btn_bind_recycler);
        btn_bind_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.onNormalClick();
            }
        });
        btn_bind_data_error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.onMultipleClick(btn_bind_data_error.getText().toString().trim());
            }
        });
        btn_bind_recycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.onRecyclerViewClick();
            }
        });
    }

    @Override
    public void clear() {
    }
    @Override
    public void clearAll() {
        this.toolBar_back = null;
        this.toolBar_title = null;
        this.btn_bind_normal = null;
        this.btn_bind_data_error = null;
        this.btn_bind_recycler = null;
    }
}
```
#### BindView BindClick
功能主要分别为绑定控件，绑定单击事件，基本上没有用，除了一些特殊应用，比如在基类中。
```
public abstract class BaseActivity<T extends Briefnessor> extends AppCompatActivity {
    T briefnessor;
    @BindView(R.id.toolBar_title)
    TextView toolBar_title;

//    @BindView({R.id.text1,R.id.text2,R.id.text3})
//    TextView[] textViews;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        briefnessor = (T) Briefness.bind(this);
        toolBar_title.setText(attachTitleRes());
    }


    @BindClick(R.id.toolBar_back)
    public void OnBackClick(View v) {
        finish();
    }
    protected abstract int attachTitleRes();

    protected void startActivity(Class<? extends AppCompatActivity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
```
该类为自动生成.
```
public class BaseActivityBriefnessor implements Briefnessor<BaseActivity> {

    @Override
    public void bind(final BaseActivity host, Object source) {
        host.toolBar_title = (TextView) host.findViewById(R.id.toolBar_title);
        host.findViewById(R.id.toolBar_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                host.OnBackClick(v);
            }
        });
    }

    @Override
    public void clear() {
    }

    @Override
    public void clearAll() {
    }
}

```