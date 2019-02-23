package com.hacknife.briefness.util;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class ViewCollection {

    public static String getFullNameByName(String name) {
        String view = "android.view." + name;
        for (String v : VIEWS) {
            if (v.equalsIgnoreCase(view)) {
                return v;
            }
        }
        String widget = "android.widget." + name;
        for (String w : WIDGETS) {
            if (w.equalsIgnoreCase(widget)) {
                return w;
            }

        }
        String webkit = "android.webkit." + name;
        for (String s : WEBKITS) {
            if (s.equalsIgnoreCase(webkit)) {
                return s;
            }
        }
        return "";
    }

    public static final String[] VIEWS = new String[]{
            "android.view.AbsSavedState",
            "android.view.ActionMode",
            "android.view.ActionProvider",
            "android.view.Choreographer",
            "android.view.CollapsibleActionView",
            "android.view.ContextMenu",
            "android.view.ContextThemeWrapper",
            "android.view.Display",
            "android.view.DragAndDropPermissions",
            "android.view.DragEvent",
            "android.view.FocusFinder",
            "android.view.FrameMetrics",
            "android.view.FrameStats",
            "android.view.GestureDetector",
            "android.view.Gravity",
            "android.view.HapticFeedbackConstants",
            "android.view.InflateException",
            "android.view.InputDevice",
            "android.view.InputEvent",
            "android.view.InputQueue",
            "android.view.KeyboardShortcutGroup",
            "android.view.KeyboardShortcutInfo",
            "android.view.KeyCharacterMap",
            "android.view.KeyEvent",
            "android.view.LayoutInflater",
            "android.view.Menu",
            "android.view.MenuInflater",
            "android.view.MenuItem",
            "android.view.MotionEvent",
            "android.view.OrientationEventListener",
            "android.view.OrientationListener",
            "android.view.PixelCopy",
            "android.view.PointerIcon",
            "android.view.ScaleGestureDetector",
            "android.view.SearchEvent",
            "android.view.SoundEffectConstants",
            "android.view.SubMenu",
            "android.view.Surface",
            "android.view.SurfaceHolder",
            "android.view.SurfaceView",
            "android.view.TextureView",
            "android.view.TouchDelegate",
            "android.view.VelocityTracker",
            "android.view.View",
            "android.view.ViewAnimationUtils",
            "android.view.ViewConfiguration",
            "android.view.ViewDebug",
            "android.view.ViewGroup",
            "android.view.ViewGroupOverlay",
            "android.view.ViewManager",
            "android.view.ViewOutlineProvider",
            "android.view.ViewOverlay",
            "android.view.ViewParent",
            "android.view.ViewPropertyAnimator",
            "android.view.ViewStructure",
            "android.view.ViewStub",
            "android.view.ViewTreeObserver",
            "android.view.Window",
            "android.view.WindowAnimationFrameStats",
            "android.view.WindowContentFrameStats",
            "android.view.WindowId",
            "android.view.WindowInsets",
            "android.view.WindowManager"
    };

    public static final String[] WIDGETS = new String[]{
            "android.widget.AbsListView",
            "android.widget.AbsoluteLayout",
            "android.widget.AbsSeekBar",
            "android.widget.AbsSpinner",
            "android.widget.ActionMenuView",
            "android.widget.Adapter",
            "android.widget.AdapterView",
            "android.widget.AdapterViewAnimator",
            "android.widget.AdapterViewFlipper",
            "android.widget.Advanceable",
            "android.widget.AlphabetIndexer",
            "android.widget.AnalogClock",
            "android.widget.ArrayAdapter",
            "android.widget.AutoCompleteTextView",
            "android.widget.BaseAdapter",
            "android.widget.BaseExpandableListAdapter",
            "android.widget.Button",
            "android.widget.CalendarView",
            "android.widget.Checkable",
            "android.widget.CheckBox",
            "android.widget.CheckedTextView",
            "android.widget.Chronometer",
            "android.widget.CompoundButton",
            "android.widget.CursorAdapter",
            "android.widget.CursorTreeAdapter",
            "android.widget.DatePicker",
            "android.widget.DialerFilter",
            "android.widget.DigitalClock",
            "android.widget.EdgeEffect",
            "android.widget.EditText",
            "android.widget.ExpandableListAdapter",
            "android.widget.ExpandableListView",
            "android.widget.Filter",
            "android.widget.Filterable",
            "android.widget.FilterQueryProvider",
            "android.widget.FrameLayout",
            "android.widget.Gallery",
            "android.widget.GridLayout",
            "android.widget.GridView",
            "android.widget.HeaderViewListAdapter",
            "android.widget.HeterogeneousExpandableList",
            "android.widget.HorizontalScrollView",
            "android.widget.ImageButton",
            "android.widget.ImageSwitcher",
            "android.widget.ImageView",
            "android.widget.LinearLayout",
            "android.widget.ListAdapter",
            "android.widget.ListPopupWindow",
            "android.widget.ListView",
            "android.widget.MediaController",
            "android.widget.MultiAutoCompleteTextView",
            "android.widget.NumberPicker",
            "android.widget.OverScroller",
            "android.widget.PopupMenu",
            "android.widget.PopupWindow",
            "android.widget.ProgressBar",
            "android.widget.QuickContactBadge",
            "android.widget.RadioButton",
            "android.widget.RadioGroup",
            "android.widget.RatingBar",
            "android.widget.RelativeLayout",
            "android.widget.RemoteViews",
            "android.widget.RemoteViewsService",
            "android.widget.ResourceCursorAdapter",
            "android.widget.ResourceCursorTreeAdapter",
            "android.widget.Scroller",
            "android.widget.ScrollView",
            "android.widget.SearchView",
            "android.widget.SectionIndexer",
            "android.widget.SeekBar",
            "android.widget.ShareActionProvider",
            "android.widget.SimpleAdapter",
            "android.widget.SimpleCursorAdapter",
            "android.widget.SimpleCursorTreeAdapter",
            "android.widget.SimpleExpandableListAdapter",
            "android.widget.SlidingDrawer",
            "android.widget.Space",
            "android.widget.Spinner",
            "android.widget.SpinnerAdapter",
            "android.widget.StackView",
            "android.widget.Switch",
            "android.widget.TabHost",
            "android.widget.TableLayout",
            "android.widget.TableRow",
            "android.widget.TabWidget",
            "android.widget.TextClock",
            "android.widget.TextSwitcher",
            "android.widget.TextView",
            "android.widget.ThemedSpinnerAdapter",
            "android.widget.TimePicker",
            "android.widget.Toast",
            "android.widget.ToggleButton",
            "android.widget.Toolbar",
            "android.widget.TwoLineListItem",
            "android.widget.VideoView",
            "android.widget.ViewAnimator",
            "android.widget.ViewFlipper",
            "android.widget.ViewSwitcher",
            "android.widget.WrapperListAdapter",
            "android.widget.ZoomButton",
            "android.widget.ZoomButtonsController",
            "android.widget.ZoomControls"
    };

    public static final String[] WEBKITS = new String[]{
            "android.webkit.ClientCertRequest",
            "android.webkit.ConsoleMessage",
            "android.webkit.CookieManager",
            "android.webkit.CookieSyncManager",
            "android.webkit.DateSorter",
            "android.webkit.DownloadListener",
            "android.webkit.GeolocationPermissions",
            "android.webkit.HttpAuthHandler",
            "android.webkit.JavascriptInterface",
            "android.webkit.JsPromptResult",
            "android.webkit.JsResult",
            "android.webkit.MimeTypeMap",
            "android.webkit.PermissionRequest",
            "android.webkit.PluginStub",
            "android.webkit.RenderProcessGoneDetail",
            "android.webkit.ServiceWorkerClient",
            "android.webkit.ServiceWorkerController",
            "android.webkit.ServiceWorkerWebSettings",
            "android.webkit.SslErrorHandler",
            "android.webkit.URLUtil",
            "android.webkit.ValueCallback",
            "android.webkit.WebBackForwardList",
            "android.webkit.WebChromeClient",
            "android.webkit.WebHistoryItem",
            "android.webkit.WebIconDatabase",
            "android.webkit.WebMessage",
            "android.webkit.WebMessagePort",
            "android.webkit.WebResourceError",
            "android.webkit.WebResourceRequest",
            "android.webkit.WebResourceResponse",
            "android.webkit.WebSettings",
            "android.webkit.WebStorage",
            "android.webkit.WebSyncManager",
            "android.webkit.WebView",
            "android.webkit.WebViewClient",
            "android.webkit.WebViewDatabase",
            "android.webkit.WebViewFragment"
    };

    public static String getValueByFullClassName(String fullClassName) {
        if (fullClassName.equals("android.widget.TextView"))
            return ".getText().toString().trim()";
        else if (fullClassName.equals("android.widget.SeekBar"))
            return ".getProgress()";
        else if (fullClassName.equals("android.widget.ProgressBar"))
            return ".getProgress()";
        else if (fullClassName.equals("android.widget.EditText"))
            return ".getText().toString().trim()";
        else if (fullClassName.equals("android.widget.CheckBox"))
            return ".isChecked()";
        return ".getText().toString().trim()";
    }
}
