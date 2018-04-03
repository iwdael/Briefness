package com.aliletter.briefness;

import android.util.Log;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/9/15.
 */

public class Briefness {
    private static final String SUFFIX = "$$Briefnessor";

    public static Briefnessor bind(Object target) {
        return bind(target, target);
    }


    public static Briefnessor bind(Object target, Object source) {
        Briefnessor proxyActivity = findProxyActivity(target);
        if (proxyActivity != null)
            proxyActivity.bind(target, source);
        Class<?> clazz = target.getClass();
        while (true) {
            clazz = clazz.getSuperclass();
            if (clazz.getName().startsWith("android.app.") | clazz.getName().startsWith("android.support.") | clazz.getName().startsWith("java.lang."))
                break;
            Briefnessor proxy = findProxySuperActivity(clazz);
            if (proxy != null) proxy.bind(target, source);
        }
        return proxyActivity;
    }

    public static void bind(Briefnessor briefnessor, Object source) {
        briefnessor.bind(source, null);
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
