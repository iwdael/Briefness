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

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BindLayout {
    int value();
}
