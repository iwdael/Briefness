package com.hacknife.briefness;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface BindClick {
    int[] value();
}
