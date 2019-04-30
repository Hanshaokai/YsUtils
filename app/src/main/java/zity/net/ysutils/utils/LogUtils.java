package zity.net.ysutils.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.utils.FileUtils;
import zity.net.basecommonmodule.utils.TimeUtils;

/**
 * author：hanshaokai
 * date： 2019/4/26 0026 11:06
 * describe：
 */

public class LogUtils {
    public LogUtils() {
        throw new UnsupportedOperationException();
    }

    public static void writeLog(String msg) {
        FileUtils.createOrExistsFile(new File(Constants.FILE_PATH_TEMP, "bracast.txt"));
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream(Constants.FILE_PATH_TEMP + "bracast.txt", true);
            fileOutputStream.write((TimeUtils.getNowString() + msg + "\n").getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
