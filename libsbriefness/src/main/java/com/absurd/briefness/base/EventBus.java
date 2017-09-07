package com.absurd.briefness.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/8/15.
 */

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventBus {
    /**
     * Author:mr-absurd
     * Depict: 监听事件的方法
     */
    String listenerSetter();

    /**
    * Author:mr-absurd
    * Depict: 监听事件的类型
    */
    Class<?> listenerType();

    /**
    * Author:mr-absurd
    * Depict: 回调的方法
    */
    String callBackMethod();
}
