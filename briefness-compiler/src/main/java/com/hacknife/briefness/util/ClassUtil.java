package com.hacknife.briefness.util;



/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */

public class ClassUtil {
    public static boolean instanceOfActivity(String qualifiedName) {
        if (qualifiedName.contains("Activity"))
            return true;
        else
            return false;
    }
}