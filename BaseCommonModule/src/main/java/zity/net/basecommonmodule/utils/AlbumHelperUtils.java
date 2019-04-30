package zity.net.basecommonmodule.utils;

/**
 * author：hanshaokai
 * date： 2018/10/17 0017 14:45
 * describe：
 */

public class AlbumHelperUtils {
    // 是不是系统相册
    public static boolean isSystemDcim(String path) {
        return path.toLowerCase().contains("dcim") || path.toLowerCase().contains("camera");
    }

    // 获取video的mine_type,暂时只支持mp4,3gp
    public static String getVideoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("mp4") || lowerPath.endsWith("mpeg4")) {
            return "video/mp4";
        } else if (lowerPath.endsWith("3gp")) {
            return "video/3gp";
        }
        return "video/mp4";
    }

    // 获取照片的mine_type
    public static String getPhotoMimeType(String path) {
        String lowerPath = path.toLowerCase();
        if (lowerPath.endsWith("jpg") || lowerPath.endsWith("jpeg")) {
            return "image/jpeg";
        } else if (lowerPath.endsWith("png")) {
            return "image/png";
        } else if (lowerPath.endsWith("gif")) {
            return "image/gif";
        }
        return "image/jpeg";
    }

    // 获得转化后的时间
    public static long getTimeWrap(long time) {
        if (time <= 0) {
            return System.currentTimeMillis();
        }
        return time;
    }

    //检测文件存在
    public static boolean checkFile(String filePath) {
        boolean result = FileUtils.isFileExists(filePath);
        return result;
    }
}
