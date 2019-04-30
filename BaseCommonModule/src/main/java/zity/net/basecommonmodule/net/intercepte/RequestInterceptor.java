package zity.net.basecommonmodule.net.intercepte;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * author：hanshaokai
 * date： 2018/5/29 16:01
 * describe： 重写拦截器
 */


public class RequestInterceptor implements Interceptor {

    private RequestHandler handler;

    public RequestInterceptor(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (handler != null) {
            request = handler.onBeforeRequest(request, chain);
        }
        Response response = chain.proceed(request);
        if (handler != null) {
            Response tmp = handler.onAfterRequest(response, chain);
            if (tmp != null) {
                return tmp;
            }
        }
        return response;
    }


}
