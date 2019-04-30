package zity.net.ysutils.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.utils.TimeUtils;
import zity.net.ysutils.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2019/4/26 0026 10:39
 * describe：
 */

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            //
            LogUtils.writeLog("系统开机");
        }
        if (intent.getAction().equals(Intent.ACTION_SHUTDOWN)) {
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(Constants.FILE_PATH_TEMP + "bracast.txt", true);
                fileOutputStream.write((TimeUtils.getNowString() + "系统退出").getBytes());
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
