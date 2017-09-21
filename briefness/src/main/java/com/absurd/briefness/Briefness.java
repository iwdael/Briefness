package com.absurd.briefness;

import android.util.Log;
import android.view.View;

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/9/15.
 */

public class Briefness {
    private static final String SUFFIX = "$$Briefnessor";

    public static void bind(Object activity) {
        Briefnessor proxyActivity = findProxyActivity(activity);
        if (proxyActivity != null)
            proxyActivity.bind(activity, activity);
        Class<?> clazz = activity.getClass();
        while (true) {
            clazz = clazz.getSuperclass();
            if (clazz.getName().startsWith("android.app.") | clazz.getName().startsWith("android.support.") | clazz.getName().startsWith("java.lang."))
                break;
            Briefnessor proxy = findProxySuperActivity(clazz);
            if (proxy != null) proxy.bind(activity, activity);
        }
    }


    public static void bind(Object activity, View view) {
        Briefnessor proxyActivity = findProxyActivity(activity);
		if (proxyActivity != null)
			proxyActivity.bind(activity, view);
        Class<?> clazz = activity.getClass();
        while (true) {
            clazz = clazz.getSuperclass();
            if (clazz.getName().startsWith("android.app.") | clazz.getName().startsWith("android.support.") | clazz.getName().startsWith("java.lang."))
                break;
            Briefnessor proxy = findProxySuperActivity(clazz);
            if (proxy != null) proxy.bind(activity, view);
        }
    }


    private static Briefnessor findProxyActivity(Object activity) {
        try {
            Class clazz = activity.getClass();
            Class briefnessClass = Class.forName(clazz.getName() + SUFFIX);
            return (Briefnessor) briefnessClass.newInstance();
        } catch (Exception e) {
        }
        Log.w("Briefness", String.format("can not find %s , something when compiler.", activity.getClass().getSimpleName() + SUFFIX));
        return null;
    }

    private static Briefnessor findProxySuperActivity(Class<?> clazz) {
        try {
            Class briefnessClass = Class.forName(clazz.getName() + SUFFIX);
            return (Briefnessor) briefnessClass.newInstance();

        } catch (Exception e) {

        }
        Log.w("Briefness", String.format("can not find %s , something when compiler.", clazz.getSimpleName() + SUFFIX));
        return null;
    }
}
