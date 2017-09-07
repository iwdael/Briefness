package com.absurd.briefness;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/8/15.
 */

public class ListenerInvocationHandler implements InvocationHandler {
    private Object mContext;
    private Map<String, Method> mMap;

    public ListenerInvocationHandler(Object mContext, Map<String, Method> mMap) {
        this.mContext = mContext;
        this.mMap = mMap;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        String name = method.getName();
        Method meth = mMap.get(name);
        if (meth == null) {
            return method.invoke(o, objects);
        } else {
            return meth.invoke(mContext, objects);
        }
    }
}
