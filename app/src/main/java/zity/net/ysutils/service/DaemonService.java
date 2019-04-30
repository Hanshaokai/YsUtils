package zity.net.ysutils.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import zity.net.ysutils.BuildConfig;
import zity.net.ysutils.R;
import zity.net.ysutils.utils.Contants;
import zity.net.ysutils.utils.LogUtils;

/**
 * 前台Service，使用startForeground
 * 这个Service尽量要轻，不要占用过多的系统资源，否则
 * 系统在资源紧张时，照样会将其杀死
 * <p>
 * Created by jianddongguo on 2017/7/7.
 * http://blog.csdn.net/andrexpert
 */
public class DaemonService extends Service {
    private static final String TAG = "DaemonService";
    public static final int NOTICE_ID = 100;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Contants.DEBUG)
            Log.d(TAG, "DaemonService---->onCreate被调用，启动前台service");
        LogUtils.writeLog("前台Service创建");
        //只考虑26及以上的
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(BuildConfig.APPLICATION_ID, "ysutils_Channel_ID", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel ID for ysutils");
            notificationManager.createNotificationChannel(notificationChannel);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BuildConfig.APPLICATION_ID);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle("KeepAppAlive");
            builder.setContentText("DaemonService is runing...");
            startForeground(NOTICE_ID, builder.build());
            // 如果觉得常驻通知栏体验不好
            // 可以通过启动CancelNoticeService，将通知移除，oom_adj值不变
            Intent intent = new Intent(this, CancelNoticeService.class);
            startForegroundService(intent);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 如果Service被终止
        // 当资源允许情况下，重启service
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 如果Service被杀死，干掉通知
        NotificationManager mManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mManager.cancel(NOTICE_ID);
        if (Contants.DEBUG)
            Log.d(TAG, "DaemonService---->onDestroy，前台service被杀死");
        LogUtils.writeLog("前台Service销毁了开始重启");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(), DaemonService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        //是在onStartCommand方法中返回START_STICKY，
        // 其作用是当Service进程被kill后，系统会尝试重新创建这个Service，
        // 且会保留Service的状态为开始状态，但不保留传递的Intent对象
        // ，onStartCommand方法一定会被重新调用；
        //其二在onDestory方法中重新启动自己，也就是说
        // ，只要Service在被销毁时走到了onDestory这里我们就重新启动它。
    }
}
