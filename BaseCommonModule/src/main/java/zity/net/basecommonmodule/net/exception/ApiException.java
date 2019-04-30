package zity.net.basecommonmodule.net.exception;

import java.io.IOException;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:45
 * describe：自定义异常类
 */


public class ApiException extends IOException {
    public ApiException(String message) {
        super(message);
    }
}
