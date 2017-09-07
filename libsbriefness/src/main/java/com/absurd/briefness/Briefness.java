package com.absurd.briefness;

import android.content.Context;
import android.view.View;

import com.absurd.briefness.base.BindClick;
import com.absurd.briefness.base.BindLayout;
import com.absurd.briefness.base.BindView;
import com.absurd.briefness.base.BindViews;
import com.absurd.briefness.base.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/8/15.
 */

public class Briefness {

    public static void bind(Context context) {
        bindLayout(context);
        bindViews(context);
        bindView(context);
        bindClick(context);
    }

    public static void bind(Object target, View view) {
        bindView(target, view);
        bindViews(target, view);
        bindClick(target, view);
    }

    private static void bindClick(Object context, View view) {


        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            BindClick bindClick = method.getAnnotation(BindClick.class);
            if (bindClick == null) continue;
            EventBus eventBus = bindClick.annotationType().getAnnotation(EventBus.class);
            if (eventBus == null) {
                continue;
            }
            String listener = eventBus.listenerSetter();
            String callBack = eventBus.callBackMethod();
            Class<?> listenerType = eventBus.listenerType();
            Map<String, Method> hashMap = new HashMap<>();
            hashMap.put(callBack, method);
            int[] resIds = bindClick.value();
            try {
                for (int resId : resIds) {
                    Method findViewById = view.getClass().getMethod("findViewById", int.class);
                    View view1 = (View) findViewById.invoke(view, resId);
                    if (view1 == null) continue;
                    Method setLinstener = view1.getClass().getMethod(listener, listenerType);
                    ListenerInvocationHandler handler = new ListenerInvocationHandler(context, hashMap);
                    Object proxy = Proxy.newProxyInstance(view.getContext().getClassLoader(), new Class[]{listenerType}, handler);
                    setLinstener.invoke(view1, proxy);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private static void bindViews(Object context, View view) {
        Class<?> clazz = context.getClass();
        Class<?> viewClazz = view.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindViews bindView = field.getAnnotation(BindViews.class);
            if (bindView == null) continue;
            int[] resIds = bindView.value();
            List<View> list = new ArrayList<>();
            try {
                for (int resId : resIds) {
                    Method findViewById = viewClazz.getMethod("findViewById", int.class);
                    View view1 = (View) findViewById.invoke(view, resId);
                    list.add(view1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            try {
                field.set(context, list);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


    }

    private static void bindView(Object context, View view) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView == null) continue;
            int resId = bindView.value();
            try {
                Method findViewById = view.getClass().getMethod("findViewById", int.class);
                View view1 = (View) findViewById.invoke(view, resId);
                field.setAccessible(true);
                field.set(context, view1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void bindLayout(Context context) {
        Class<?> clazz = context.getClass();
        BindLayout bindLayout = clazz.getAnnotation(BindLayout.class);
        if (bindLayout == null) return;
        int resId = bindLayout.value();
        try {
            Method setContentView = clazz.getMethod("setContentView", int.class);
            setContentView.invoke(context, resId);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void bindView(Context context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindView bindView = field.getAnnotation(BindView.class);
            if (bindView == null) continue;
            int resId = bindView.value();
            try {
                Method findViewById = clazz.getMethod("findViewById", int.class);
                View view = (View) findViewById.invoke(context, resId);
                field.setAccessible(true);
                field.set(context, view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void bindViews(Context context) {
        Class<?> clazz = context.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            BindViews bindView = field.getAnnotation(BindViews.class);
            if (bindView == null) continue;
            int[] resIds = bindView.value();
            List<View> list = new ArrayList<>();
            try {
                for (int resId : resIds) {
                    Method findViewById = clazz.getMethod("findViewById", int.class);
                    View view = (View) findViewById.invoke(context, resId);
                    list.add(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            field.setAccessible(true);
            try {
                field.set(context, list);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void bindClick(Context context) {
        Class<?> clazz = context.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            BindClick bindClick = method.getAnnotation(BindClick.class);
            if (bindClick == null) continue;
            EventBus eventBus = bindClick.annotationType().getAnnotation(EventBus.class);
            if (eventBus == null) {
                continue;
            }
            String listener = eventBus.listenerSetter();
            String callBack = eventBus.callBackMethod();
            Class<?> listenerType = eventBus.listenerType();
            Map<String, Method> hashMap = new HashMap<>();
            hashMap.put(callBack, method);
            int[] resIds = bindClick.value();
            try {
                for (int resId : resIds) {
                    Method findViewById = clazz.getMethod("findViewById", int.class);
                    View view = (View) findViewById.invoke(context, resId);
                    if (view == null) continue;
                    Method setLinstener = view.getClass().getMethod(listener, listenerType);
                    ListenerInvocationHandler handler = new ListenerInvocationHandler(context, hashMap);
                    Object proxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{listenerType}, handler);
                    setLinstener.invoke(view, proxy);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
