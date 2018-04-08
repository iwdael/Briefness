package briefness;

import android.view.View;
import android.widget.TextView;

import com.blackchopper.briefness.Injector;

/**
 * author  : Black Chopper
 * e-mail  : 4884280@qq.com
 * github  : http://github.com/BlackChopper
 * project : Briefness
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
