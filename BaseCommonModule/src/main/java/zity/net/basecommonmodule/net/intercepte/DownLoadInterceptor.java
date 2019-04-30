package zity.net.basecommonmodule.net.intercepte;

import zity.net.basecommonmodule.net.download.DownLoadResponseBody;
import zity.net.basecommonmodule.net.listener.DownLoadProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import zity.net.basecommonmodule.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:48
 * describe： 两种写法
 */


public class DownLoadInterceptor implements Interceptor {
    private DownLoadProgressListener loadProgressListener;

    public DownLoadInterceptor(DownLoadProgressListener loadProgressListener) {
        this.loadProgressListener = loadProgressListener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        LogUtils.d("intercept" + originalResponse.message());
        return originalResponse.newBuilder()
                .body(new DownLoadResponseBody(originalResponse.body(), loadProgressListener)).build();
    }
}

