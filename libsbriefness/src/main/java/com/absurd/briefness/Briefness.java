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

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/8/15.
 */

public class Briefness {

    public static void bind(Context context) {
        bindLayout(context);
        bindView(context);
        bindViews(context);
        bindClick(context);
    }

    public static void bind(Object target, View view) {
        bindView(target, view);
        bindViews(target, view);
        bindClick(target, view);
    }

    private static void bindClick(Object context, View view) {
        Class clazz = context.getClass();
        Method[] methods = clazz.getMethods();
        Method[] var4 = methods;
        int var5 = methods.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Method method = var4[var6];
            BindClick bindClick = (BindClick)method.getAnnotation(BindClick.class);
            if(bindClick != null) {
                EventBus eventBus = (EventBus)bindClick.annotationType().getAnnotation(EventBus.class);
                if(eventBus != null) {
                    String listener = eventBus.listenerSetter();
                    String callBack = eventBus.callBackMethod();
                    Class listenerType = eventBus.listenerType();
                    HashMap hashMap = new HashMap();
                    hashMap.put(callBack, method);
                    int[] resIds = bindClick.value();

                    try {
                        int[] e = resIds;
                        int var16 = resIds.length;

                        for(int var17 = 0; var17 < var16; ++var17) {
                            int resId = e[var17];
                            Method findViewById = view.getClass().getMethod("findViewById", new Class[]{Integer.TYPE});
                            View view1 = (View)findViewById.invoke(view, new Object[]{Integer.valueOf(resId)});
                            if(view1 != null) {
                                Method setLinstener = view1.getClass().getMethod(listener, new Class[]{listenerType});
                                ListenerInvocationHandler handler = new ListenerInvocationHandler(context, hashMap);
                                Object proxy = Proxy.newProxyInstance(view.getContext().getClassLoader(), new Class[]{listenerType}, handler);
                                setLinstener.invoke(view1, new Object[]{proxy});
                            }
                        }
                    } catch (Exception var24) {
                        var24.printStackTrace();
                    }
                }
            }
        }

    }

    private static void bindViews(Object context, View view) {
        Class clazz = context.getClass();
        Class viewClazz = view.getClass();
        Field[] fields = clazz.getFields();
        Field[] var5 = fields;
        int var6 = fields.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Field field = var5[var7];
            BindViews bindView = (BindViews)field.getAnnotation(BindViews.class);
            if(bindView != null) {
                int[] resIds = bindView.value();
                ArrayList list = new ArrayList();

                try {
                    int[] e = resIds;
                    int var13 = resIds.length;

                    for(int var14 = 0; var14 < var13; ++var14) {
                        int resId = e[var14];
                        Method findViewById = viewClazz.getMethod("findViewById", new Class[]{Integer.TYPE});
                        View view1 = (View)findViewById.invoke(view, new Object[]{Integer.valueOf(resId)});
                        list.add(view1);
                    }
                } catch (Exception var19) {
                    var19.printStackTrace();
                }

                field.setAccessible(true);

                try {
                    field.set(context, list);
                } catch (IllegalAccessException var18) {
                    var18.printStackTrace();
                }
            }
        }

    }

    private static void bindView(Object context, View view) {
        Class clazz = context.getClass();
        Field[] fields = clazz.getFields();
        Field[] var4 = fields;
        int var5 = fields.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Field field = var4[var6];
            BindView bindView = (BindView)field.getAnnotation(BindView.class);
            if(bindView != null) {
                int resId = bindView.value();

                try {
                    Method e = view.getClass().getMethod("findViewById", new Class[]{Integer.TYPE});
                    View view1 = (View)e.invoke(view, new Object[]{Integer.valueOf(resId)});
                    field.setAccessible(true);
                    field.set(context, view1);
                } catch (Exception var12) {
                    var12.printStackTrace();
                }
            }
        }

    }

    private static void bindLayout(Context context) {
        Class clazz = context.getClass();
        BindLayout bindLayout = (BindLayout)clazz.getAnnotation(BindLayout.class);
        if(bindLayout != null) {
            int resId = bindLayout.value();

            try {
                Method e = clazz.getMethod("setContentView", new Class[]{Integer.TYPE});
                e.invoke(context, new Object[]{Integer.valueOf(resId)});
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }

    private static void bindView(Context context) {
        Class clazz = context.getClass();
        Field[] fields = clazz.getFields();
        Field[] var3 = fields;
        int var4 = fields.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            BindView bindView = (BindView)field.getAnnotation(BindView.class);
            if(bindView != null) {
                int resId = bindView.value();

                try {
                    Method e = clazz.getMethod("findViewById", new Class[]{Integer.TYPE});
                    View view = (View)e.invoke(context, new Object[]{Integer.valueOf(resId)});
                    field.setAccessible(true);
                    field.set(context, view);
                } catch (Exception var11) {
                    var11.printStackTrace();
                }
            }
        }

    }

    private static void bindViews(Context context) {
        Class clazz = context.getClass();
        Field[] fields = clazz.getFields();
        Field[] var3 = fields;
        int var4 = fields.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            BindViews bindView = (BindViews)field.getAnnotation(BindViews.class);
            if(bindView != null) {
                int[] resIds = bindView.value();
                ArrayList list = new ArrayList();

                try {
                    int[] e = resIds;
                    int var11 = resIds.length;

                    for(int var12 = 0; var12 < var11; ++var12) {
                        int resId = e[var12];
                        Method findViewById = clazz.getMethod("findViewById", new Class[]{Integer.TYPE});
                        View view = (View)findViewById.invoke(context, new Object[]{Integer.valueOf(resId)});
                        list.add(view);
                    }
                } catch (Exception var17) {
                    var17.printStackTrace();
                }

                field.setAccessible(true);

                try {
                    field.set(context, list);
                } catch (IllegalAccessException var16) {
                    var16.printStackTrace();
                }
            }
        }

    }

    private static void bindClick(Context context) {
        Class clazz = context.getClass();
        Method[] methods = clazz.getMethods();
        Method[] var3 = methods;
        int var4 = methods.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];
            BindClick bindClick = (BindClick)method.getAnnotation(BindClick.class);
            if(bindClick != null) {
                EventBus eventBus = (EventBus)bindClick.annotationType().getAnnotation(EventBus.class);
                if(eventBus != null) {
                    String listener = eventBus.listenerSetter();
                    String callBack = eventBus.callBackMethod();
                    Class listenerType = eventBus.listenerType();
                    HashMap hashMap = new HashMap();
                    hashMap.put(callBack, method);
                    int[] resIds = bindClick.value();

                    try {
                        int[] e = resIds;
                        int var15 = resIds.length;

                        for(int var16 = 0; var16 < var15; ++var16) {
                            int resId = e[var16];
                            Method findViewById = clazz.getMethod("findViewById", new Class[]{Integer.TYPE});
                            View view = (View)findViewById.invoke(context, new Object[]{Integer.valueOf(resId)});
                            if(view != null) {
                                Method setLinstener = view.getClass().getMethod(listener, new Class[]{listenerType});
                                ListenerInvocationHandler handler = new ListenerInvocationHandler(context, hashMap);
                                Object proxy = Proxy.newProxyInstance(context.getClassLoader(), new Class[]{listenerType}, handler);
                                setLinstener.invoke(view, new Object[]{proxy});
                            }
                        }
                    } catch (Exception var23) {
                        var23.printStackTrace();
                    }
                }
            }
        }

    }
}
