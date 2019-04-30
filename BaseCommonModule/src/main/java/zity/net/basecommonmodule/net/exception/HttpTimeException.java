package zity.net.basecommonmodule.net.exception;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:46
 * describe：
 */


public class HttpTimeException extends RuntimeException {
    public static final int NO_DATA = 0x2;

    public HttpTimeException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public HttpTimeException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 转换错误数据
     */

    public static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case NO_DATA:
                message = "无数据";
                break;
            default:
                message = "error";
                break;
        }
        return message;
    }
}
