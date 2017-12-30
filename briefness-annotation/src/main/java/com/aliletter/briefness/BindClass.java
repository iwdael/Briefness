package com.aliletter.briefness;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/30
 */

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface BindClass {
    String clazz();

    String name();

}
