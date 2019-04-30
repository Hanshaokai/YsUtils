package zity.net.ysutils.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import zity.net.ysutils.R;
import zity.net.ysutils.utils.Contants;
import zity.net.ysutils.utils.LogUtils;

/**循环播放一段无声音频，以提升进程优先级
 *
 * Created by jianddongguo on 2017/7/11.
 * http://blog.csdn.net/andrexpert
 */

public class PlayerMusicService extends Service {
    private final static String TAG = "PlayerMusicService";
    private MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Contants.DEBUG)
            Log.d(TAG,TAG+"---->onCreate,启动服务");
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                startPlayMusic();
            }
        }).start();
        return START_STICKY;
    }

    private void startPlayMusic(){
        if(mMediaPlayer != null){
            if(Contants.DEBUG)
                Log.d(TAG,"启动后台播放音乐");
            LogUtils.writeLog("开始启动后台音乐");
            mMediaPlayer.start();
        }
    }

    private void stopPlayMusic(){
        if(mMediaPlayer != null){
            if(Contants.DEBUG)
                Log.d(TAG,"关闭后台播放音乐");
            mMediaPlayer.stop();
            LogUtils.writeLog("后台音乐关闭");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayMusic();
        if(Contants.DEBUG)
            Log.d(TAG,TAG+"---->onCreate,停止服务");
        // 重启自己
        Intent intent = new Intent(getApplicationContext(),PlayerMusicService.class);
        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}
