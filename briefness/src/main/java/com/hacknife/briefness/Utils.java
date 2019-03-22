package com.hacknife.briefness;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * author  : Hacknife
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/hacknife
 * project : Briefness
 */
public class Utils {
    public static boolean contentViewNoExist(Object activity) {
        ViewGroup content = ((Activity)activity).findViewById(android.R.id.content);
        if (content.getChildCount() == 0)
            return true;
        else
            return false;
    }
}
