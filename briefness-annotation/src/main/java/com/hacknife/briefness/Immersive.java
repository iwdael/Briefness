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
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.TYPE)
public @interface Immersive {
    int statusColor() default 0;

    int navigationColor() default 0;

    boolean statusEmbed() default false;

    boolean navigationEmbed() default false;
}
