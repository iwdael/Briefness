package com.blackchopper.briefness;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public class BriefnessInjector {
    public static void injector(View view, Object value) {
        if (value == null | view == null) {
        } else if (throwInjector(view, value)) {
        } else if (view instanceof ImageView) {
            injectImageView((ImageView) view, value);
        } else if (view instanceof Button) {
            injectButton((Button) view, value);
        } else if (view instanceof EditText) {
            injectEditText((EditText) view, value);
        } else if (view instanceof TextView) {
            injectTextView((TextView) view, value);
        } else {
            Log.e("Briefness", "No match method and can not inject " + view.getClass().getSimpleName());
        }
    }

    private static boolean throwInjector(View view, Object value) {
        try {
            String packageName = view.getContext().getPackageName();
            Class<?> clazz = Class.forName(packageName + ".briefness.ViewInjector");
            Method injector = clazz.getDeclaredMethod("injector", View.class, Object.class);
            return (boolean) injector.invoke(null, view, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void injectEditText(EditText view, Object value) {
        if (value instanceof String) {
            view.setText((String) value);
        }
    }


    private static void injectButton(Button view, Object value) {
        if (value instanceof String) {
            view.setText((String) value);
        }
    }

    private static void injectImageView(ImageView view, Object value) {
        if (value instanceof Bitmap) {
            view.setImageBitmap((Bitmap) value);
        }
    }

    private static void injectTextView(TextView view, Object value) {
        if (value instanceof String) {
            view.setText((String) value);
        }
    }


}
