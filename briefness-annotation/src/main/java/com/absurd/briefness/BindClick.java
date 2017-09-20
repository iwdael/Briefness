package com.absurd.briefness;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: mr-absurd
 * Github: http://github.com/mr-absurd
 * Data: 2017/9/16.
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BindClick {
    int[] value();
}
