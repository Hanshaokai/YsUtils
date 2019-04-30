package zity.net.basecommonmodule.utils;

import android.graphics.Color;
import android.widget.Button;


/**
 * author：hanshaokai
 * date： 2018/5/16 10:41
 * describe：
 */


public class ViewUtils {
    private ViewUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public static void setBtnEable(Button button, Boolean isEable) {
        button.setEnabled(isEable);
        if (!isEable) {
            button.setBackgroundColor(Color.LTGRAY);
        } else {
            button.setBackgroundColor(Color.BLACK);
        }
    }
}
