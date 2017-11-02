package com.aliletter.briefness;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: aliletter
 * Github: http://github.com/aliletter
 * Data: 2017/9/16.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BindClick {
    int[] value();
}
