package com.blackchopper.briefness;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
 */
public class Utils {
    public static boolean contentViewExist(Activity activity) {
        ViewGroup content = activity.findViewById(android.R.id.content);
        if (content.getChildCount() == 0)
            return false;
        else
            return true;
    }
}
