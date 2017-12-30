package com.aliletter.briefness;

import android.view.View;
import android.widget.TextView;

/**
 * Author: aliletter
 * Github：https://github.com/aliletter
 * Emile:4884280@qq.com
 */

public class BriefnessInjector implements Injector {
    @Override
    public void inject(View view, Object value) {
        if (view instanceof TextView) {
            if (value instanceof String) {
                ((TextView) view).setText((String) value);
            }
        }
    }
}
