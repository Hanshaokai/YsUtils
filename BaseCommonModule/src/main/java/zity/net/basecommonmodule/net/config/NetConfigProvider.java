package zity.net.basecommonmodule.net.config;

import zity.net.basecommonmodule.net.intercepte.RequestHandler;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:41
 * describe： 每种请求可通过此类配置 请求链接参数
 */


public interface NetConfigProvider {
    /**
     * 过滤器参数
     * @return
     */
    Interceptor[] configInterceptors();

    /**
     * 连接参数
     * @param builder
     */
    void configHttps(OkHttpClient.Builder builder);

    /**
     * cookie 参数
     * @return
     */
    CookieJar configCookie();

    /**
     * handler参数
     * @return
     */
    RequestHandler configHandler();

    /**
     * 链接时间
     * @return
     */
    long configConnectTimeoutSecs();

    /**
     * 读取时间
     * @return
     */
    long configReadTimeoutSecs();

    /**
     * 写入时间
     * @return
     */
    long configWriteTimeoutSecs();

    /**
     * 是否log 配置
     * @return
     */
    boolean configLogEnable();
}
