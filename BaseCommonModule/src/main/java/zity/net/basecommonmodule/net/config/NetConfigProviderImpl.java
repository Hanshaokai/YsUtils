package zity.net.basecommonmodule.net.config;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import zity.net.basecommonmodule.BuildConfig;
import zity.net.basecommonmodule.net.intercepte.DefaultHandlerImpl;
import zity.net.basecommonmodule.net.intercepte.RequestHandler;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:41
 * describe：
 */


public class NetConfigProviderImpl implements NetConfigProvider {

    private long CONNECT_TIME_OUT = 8 * 1000L;
    private long READ_TIME_OUT = 10 * 1000L;
    private long WRITE_TIME_OUT = 10 * 1000L;

    public void setCONNECT_TIME_OUT(long CONNECT_TIME_OUT, long READ_TIME_OUT, long WRITE_TIME_OUT) {
        this.CONNECT_TIME_OUT = CONNECT_TIME_OUT;
        this.READ_TIME_OUT = READ_TIME_OUT;
        this.WRITE_TIME_OUT = WRITE_TIME_OUT;
    }

    @Override
    public Interceptor[] configInterceptors() {
        return null;
    }

    @Override
    public void configHttps(OkHttpClient.Builder builder) {

    }

    @Override
    public CookieJar configCookie() {
        return null;
    }

    @Override
    public RequestHandler configHandler() {

        return new DefaultHandlerImpl();
    }

    @Override
    public long configConnectTimeoutSecs() {
        return CONNECT_TIME_OUT;
    }

    @Override
    public long configReadTimeoutSecs() {
        return READ_TIME_OUT;
    }

    @Override
    public long configWriteTimeoutSecs() {
        return WRITE_TIME_OUT;
    }

    @Override
    public boolean configLogEnable() {
        return BuildConfig.DEBUG;
    }
}


