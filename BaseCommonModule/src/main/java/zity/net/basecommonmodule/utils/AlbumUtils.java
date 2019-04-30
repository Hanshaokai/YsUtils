package zity.net.basecommonmodule.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import java.io.File;

import static zity.net.basecommonmodule.utils.AlbumHelperUtils.checkFile;
import static zity.net.basecommonmodule.utils.AlbumHelperUtils.getPhotoMimeType;
import static zity.net.basecommonmodule.utils.AlbumHelperUtils.getTimeWrap;

/**
 * author：hanshaokai
 * date： 2018/10/17 0017 13:58
 * describe：
 */

public class AlbumUtils {


    public static void notifyScanDcim(Context context, String filePath) {
        scanFile(context, filePath);
    }

    public static void insertVideoMediaStore(Context context, String filepath,
                                             long dateTaken,
                                             long duration) {
        inserVideoToMediaStore(context, filepath, dateTaken, 0, 0, duration);
    }

    public static void insertImageToMediaStore(Context context, String filePath
            , long createTime) {
        insertImageToMediaStore(context, filePath, createTime, 0, 0);
    }

    /**
     * 针对系统文件只需要扫描，不用插入内容提供者，不然重复
     *
     * @param context
     * @param filePath
     */
    private static void scanFile(Context context, String filePath) {
        if (!FileUtils.isFile(filePath)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(new File(filePath)));
        context.sendBroadcast(intent);
    }

    /**
     * 保存到非系统资源相册中时，需要进行ContentProvider 的插入更新，来达到可以在相册中显示的目的
     * 插入时初始化公共字段
     *
     * @param filePath
     * @param time
     * @return
     */
    private static ContentValues initCommonContentValues(String filePath, long time) {
        ContentValues values = new ContentValues();
        File saveFile = new File(filePath);
        long timeMillis = getTimeWrap(time);
        values.put(MediaStore.MediaColumns.TITLE, saveFile.getName());
        values.put(MediaStore.MediaColumns.DISPLAY_NAME, saveFile.getName());
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, timeMillis);
        values.put(MediaStore.MediaColumns.DATE_ADDED, timeMillis);
        values.put(MediaStore.MediaColumns.DATA, saveFile.getAbsolutePath());
        values.put(MediaStore.MediaColumns.SIZE, saveFile.length());
        return values;
    }

    /**
     * 保存到照片到本地，并插入MediaStore以保证相册可以查看到
     * 这是更优化的方法，防止读取的照片获取不到宽高
     *
     * @param context
     * @param filePath   文件路径
     * @param createTime 创建时间《=0为当前时间ms
     * @param width      宽度
     * @param height     高度
     */
    private static void insertImageToMediaStore(Context context,
                                                String filePath, long createTime, int width, int height
    ) {
        if (!FileUtils.isFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Images.ImageColumns.DATE_TAKEN, createTime);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        values.put(MediaStore.Images.ImageColumns.ORIENTATION, 0);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0) values.put(MediaStore.Images.ImageColumns.WIDTH, 0);
            if (height > 0) values.put(MediaStore.Images.ImageColumns.HEIGHT, 0);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, getPhotoMimeType(filePath));
        context.getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
    }

    private static void inserVideoToMediaStore(Context context, String filePath
            , long createTime, int width, int height, long duration) {
        if (!checkFile(filePath))
            return;
        createTime = getTimeWrap(createTime);
        ContentValues values = initCommonContentValues(filePath, createTime);
        values.put(MediaStore.Video.VideoColumns.DATE_TAKEN, createTime);
        if (duration > 0)
            values.put(MediaStore.Video.VideoColumns.DURATION, duration);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            if (width > 0) values.put(MediaStore.Video.VideoColumns.WIDTH, width);
            if (height > 0) values.put(MediaStore.Video.VideoColumns.HEIGHT, height);
        }
        values.put(MediaStore.MediaColumns.MIME_TYPE, AlbumHelperUtils.getVideoMimeType(filePath));
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values);
    }

}
