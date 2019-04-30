package zity.net.basecommonmodule.net.intercepte;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:44
 * describe：
 */


public interface RequestHandler {
    /**
     * 请求前
     *
     * @param request
     * @param chain
     * @return
     */
    Request onBeforeRequest(Request request, Interceptor.Chain chain);

    /**
     * 请求后
     *
     * @param response
     * @param chain
     * @return
     * @throws IOException
     */
    Response onAfterRequest(Response response, Interceptor.Chain chain) throws IOException;
}