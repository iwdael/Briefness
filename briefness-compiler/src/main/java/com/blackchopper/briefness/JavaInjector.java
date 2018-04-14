package com.blackchopper.briefness;

import com.blackchopper.briefness.databinding.JavaLayout;
import com.blackchopper.briefness.util.ClassUtil;
import com.blackchopper.briefness.util.FileUtil;

import java.io.File;

import static com.blackchopper.briefness.XmlProxyInfo.SPLIT;
import static com.blackchopper.briefness.XmlProxyInfo.readTextFile;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

public class JavaInjector {
    public static final String PACKAGE_NAME = ClassUtil.findPackageName();
    public static final String PACKAGE = "package com.blackchopper.briefness;\n";
    public static final String IMPORT = "import android.graphics.Bitmap;\n" +
            "import android.util.Log;\n" +
            "import android.view.View;\n" +
            "import android.widget.Button;\n" +
            "import android.widget.EditText;\n" +
            "import android.widget.ImageView;\n" +
            "import android.widget.TextView;\n" +
            "import " + PACKAGE_NAME + ".briefness.ViewInjector;\n";

    public static final String CLASS = "public class BriefnessInjector {\n" +
            "    public static void injector(boolean fileter,View view, Object value) {\n" +
            "        if (value == null | view == null) {\n" +
            "        } else if (fileter) {\n" +
            "        } else if (view instanceof ImageView) {\n" +
            "            injectImageView((ImageView) view, value);\n" +
            "        } else if (view instanceof Button) {\n" +
            "            injectButton((Button) view, value);\n" +
            "        } else if (view instanceof EditText) {\n" +
            "            injectEditText((EditText) view, value);\n" +
            "        } else if (view instanceof TextView) {\n" +
            "            injectTextView((TextView) view, value);\n" +
            "        } else {\n" +
            "            Log.e(\"Briefness\", \"No match method and can not inject \" + view.getClass().getSimpleName());\n" +
            "        }\n" +
            "    }\n" +
            "\n" +
            "    private static void injectEditText(EditText view, Object value) {\n" +
            "        if (value instanceof String) {\n" +
            "            view.setText((String) value);\n" +
            "        }  \n" +
            "    }\n" +
            "\n" +
            "\n" +
            "    private static void injectButton(Button view, Object value) {\n" +
            "        if (value instanceof String) {\n" +
            "            view.setText((String) value);\n" +
            "        } \n" +
            "    }\n" +
            "\n" +
            "    private static void injectImageView(ImageView view, Object value) {\n" +
            "        if (value instanceof Bitmap) {\n" +
            "            view.setImageBitmap((Bitmap) value);\n" +
            "        } \n" +
            "    }\n" +
            "\n" +
            "    private static void injectTextView(TextView view, Object value) {\n" +
            "        if (value instanceof String) {\n" +
            "            view.setText((String) value);\n" +
            "        }  \n" +
            "    }\n" +
            "\n" +
            "  \n" +
            "}\n";

    public static final String BRIEFNESS_INJECTOR = "// This class and method can not be confused and modified ！\n" +
            "package " + PACKAGE_NAME + ".briefness;\n" +
            "\n" +
            "import android.view.View;\n" +
            "\n" +
            "/**\n" +
            " * author  : Black Chopper\n" +
            " * e-mail  : 4884280@qq.com\n" +
            " * github  : http://github.com/BlackChopper\n" +
            " * project : Briefness\n" +
            " */\n" +
            "public class ViewInjector {\n" +
            "    public static boolean injector(View view, Object value) {\n" +
            "        return false;\n" +
            "    }\n" +
            "}";
    boolean debug = false;

    public void witeCode() {


        String module = readTextFile(System.getProperty("user.dir") + "/BriefnessConfig");

        String java = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT
                + "src/main/java/"
                + PACKAGE_NAME.replace(".", "/")
                + "/briefness/ViewInjector.java";

        if (!new File(java).exists())
            FileUtil.createFile(java, BRIEFNESS_INJECTOR);
    }

    public boolean isBriefnessInjectorExits() {
        String module = readTextFile(System.getProperty("user.dir") + "/BriefnessConfig");

        String java = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT
                + "build/generated/source/apt/debug/"
                + "com.blackchopper.briefness".replace(".", "/")
                + "/BriefnessInjector.java";
        boolean flag;
        if (new File(java).exists()) {
            flag = true;
        } else {
            flag = false;
        }
        java = System.getProperty("user.dir") + SPLIT + module.replace(" ", "").replace("/", "") + SPLIT
                + "build/generated/source/apt/release/"
                + "com.blackchopper.briefness".replace(".", "/")
                + "/BriefnessInjector.java";
        if (new File(java).exists()) {
            flag = true | flag;
        } else {
            flag = false | flag;
        }
        return flag;
    }

    public String getBriefnessInjectorCode() {
        StringBuilder builder = new StringBuilder();
        return builder.append(PACKAGE)
                .append(IMPORT)
                .append(JavaLayout.author)
                .append(CLASS).toString();
    }
}
