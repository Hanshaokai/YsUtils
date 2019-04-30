package zity.net.ysutils.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import zity.net.basecommonmodule.utils.Utils;
import zity.net.ysutils.R;

/**
 * author：hanshaokai
 * date： 2019/4/25 0025 18:42
 * describe：
 */

public class ToastUtils {
    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    public ToastUtils() {
        throw new UnsupportedOperationException("u can not instant it");
    }

    public static void showLong(final String msg) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                LayoutInflater layoutInflaterCompat = (LayoutInflater) Utils.getApp().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = layoutInflaterCompat.inflate(R.layout.custom_toast_view, null, false);
                TextView msgview = view.findViewById(R.id.tv_alert_msg);
                final Toast toast = new Toast(Utils.getApp());
                toast.setView(view);
                msgview.setText(msg);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

}
