package com.aliletter.briefness;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Method;

/**
 * Author: aliletter
 * Github：https://github.com/aliletter
 * Emile:4884280@qq.com
 */

public class ViewInjector {
    public static void inject(View view, Object value) {
        if (value == null | view == null) {
        } else if (view instanceof ImageView) {
            injectImageView((ImageView) view, value);
        } else if (view instanceof Button) {
            injectButton((Button) view, value);
        } else if (view instanceof EditText) {
            injectEditText((EditText) view, value);
        } else if (view instanceof TextView) {
            injectTextView((TextView) view, value);
        } else {
            throwInjector(view, value);
        }
    }

    private static void injectEditText(EditText view, Object value) {
        if (value instanceof String) {
            view.setText((String) value);
        } else {
            throwInjector(view, value);
        }
    }


    private static void injectButton(Button view, Object value) {
        if (value instanceof String) {
            view.setText((String) value);
        } else {
            throwInjector(view, value);
        }
    }

    private static void injectImageView(ImageView view, Object value) {
        if (value instanceof Bitmap) {
            view.setImageBitmap((Bitmap) value);
        } else {
            throwInjector(view, value);
        }
    }

    private static void injectTextView(TextView view, Object value) {
        if (value instanceof String) {
            view.setText((String) value);
        } else {
            throwInjector(view, value);
        }
    }

    private static void throwInjector(View view, Object value) {

        try {
            Log.v("Briefness", "can not inject " + view.getClass().getSimpleName());
            Class<?> injector = Class.forName("com.aliletter.briefness.BriefnessInjector");
            Method inject = injector.getDeclaredMethod("inject", new Class[]{View.class, Object.class});
            inject.invoke(injector.newInstance(), new Object[]{view, value});
        } catch (Exception e) {
            Log.e("Briefness", "not find BriefnessInjector and can not inject " + view.getClass().getSimpleName());
            e.printStackTrace();
        }
    }
}
