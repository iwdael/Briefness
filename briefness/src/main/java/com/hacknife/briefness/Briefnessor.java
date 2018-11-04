package com.hacknife.briefness;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public interface Briefnessor<T> {
    void bind(T target, Object source);

    void clear();

    void clearAll();

    void bindViewModel(Object viewModel);
}
