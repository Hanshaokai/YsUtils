package zity.net.ysutils.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import androidx.annotation.Nullable;

import zity.net.ysutils.receiver.ScreenReceiver;
import zity.net.ysutils.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2019/4/26 0026 11:02
 * describe：
 */

public class RegisterService extends Service {

    private ScreenReceiver mScreenReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mScreenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(mScreenReceiver, filter);
        LogUtils.writeLog("注册广播");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenReceiver);
    }
}
