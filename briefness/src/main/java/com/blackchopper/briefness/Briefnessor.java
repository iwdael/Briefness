package com.blackchopper.briefness;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */

public interface Briefnessor<T> {
    void bind(T target, Object source);
}
