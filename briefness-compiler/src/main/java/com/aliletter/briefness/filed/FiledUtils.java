package com.aliletter.briefness.filed;

/**
 * Author：alilettter
 * Github: http://github.com/aliletter
 * Email: 4884280@qq.com
 * data: 2017/12/30
 */

public class FiledUtils {

    public static String getMethod(String viewType, String valueType) {
        if (valueType.equalsIgnoreCase("android.widget.TextView"))
            return "setText";
        return "setText";
    }
}
