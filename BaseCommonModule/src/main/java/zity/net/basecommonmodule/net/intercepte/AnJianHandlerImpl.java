package zity.net.basecommonmodule.net.intercepte;

import android.app.Activity;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import zity.net.basecommonmodule.commonbase.BaseResponse;
import zity.net.basecommonmodule.net.exception.ApiException;
import zity.net.basecommonmodule.utils.ActivityUtils;
import zity.net.basecommonmodule.utils.LogUtils;
import zity.net.basecommonmodule.utils.NetworkUtils;
import zity.net.basecommonmodule.utils.ToastUtils;

/**
 * author：hanshaokai
 * date： 2019/1/16 0016 14:08
 * describe：
 */

public class AnJianHandlerImpl implements RequestHandler {

    @Override
    public Request onBeforeRequest(Request request, Interceptor.Chain chain) throws UnsupportedOperationException {
        //请求前判断网络
        if (!NetworkUtils.isConnected()) {
            ActivityUtils.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("网络未连接");
                }
            });
            throw new UnsupportedOperationException("断网");
        } else {
            LogUtils.d(request.url().toString());
            //如果表头中需要加参数如下
        /*chain.request().newBuilder()
                .addHeader("Token", Constants.APP_TOKEN)
                .addHeader("Authorization", "")
                .build();*/
            //公共参数
            String method = request.method();
            //String token = SPUtils.getInstance().getString(Constants.APP_TOKEN_KEY);  //暂时不传
            if ("GET".equals(method)) {
                // TODO: 2018/6/28 0028  暂时不传token
           /* HttpUrl mHttpUrl = request.url().newBuilder().addQueryParameter("token", token).build();
            request = request.newBuilder().url(mHttpUrl).build();*/
            } else if ("POST".equals(method)) {
                RequestBody body = request.body();
                if (body instanceof FormBody) {
                    //实例走这里表单传输
                    FormBody.Builder newFormBody = new FormBody.Builder();
                    for (int i = 0; i < ((FormBody) body).size(); i++) {
                        newFormBody.addEncoded(((FormBody) body).encodedName(i), ((FormBody) body).value(i));
                    }
                    // TODO: 2018/6/28 0028  暂时不传token
                    //newFormBody.add("token", token);
                    request = request.newBuilder().method(request.method(), newFormBody.build()).build();
                    LogUtils.d(this.getClass().getSimpleName(), "Post 表单传值");
                } else {
                    // 文件上传走这里
                    LogUtils.d(this.getClass().getSimpleName(), "json数据传值");
                    Buffer buffer = new Buffer();
                    try {
                        body.writeTo(buffer);
                        String oldJsonParams = buffer.readUtf8();
                        LogUtils.d("oldJsonParams: " + oldJsonParams);
                  /*  Gson gson = new Gson();
                    HashMap<String, Object> jsonObject2 = gson.fromJson(oldJsonParams, HashMap.class);

                    // jsonObject2.put("token", token);
                    request = request.newBuilder()
                            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gson.toJson(jsonObject2)))
                            .build();
                    LogUtils.d("Post" + request.url().toString());*/

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return request;
    }

    @Override
    public Response onAfterRequest(Response response, Interceptor.Chain chain) throws IOException {
        ApiException e = null;
        final Activity activity = ActivityUtils.getTopActivity();
        //请求返回码对应处理
        if (401 == response.code()) {
            throw new UnsupportedOperationException("登录已过期,请重新登录!");
        } else if (403 == response.code()) {
            throw new UnsupportedOperationException("禁止访问!");
        } else if (404 == response.code()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showShort("链接错误");
                }
            });
            throw new UnsupportedOperationException("404");
        } else if (503 == response.code()) {
            throw new UnsupportedOperationException("禁止访问!");
        } else if (500 == response.code()) {
            throw new UnsupportedOperationException("禁止访问!");
        } else if (200 == response.code()) {
            //请求正常走这里
            //只能调一次下一次 清除了缓存
            BufferedSource source = response.body().source();
            source.request(Long.MAX_VALUE); // request the entire body
            String resultBuffer = source.buffer().clone().readString(Charset.forName("UTF-8"));
            //下方 无法在release版下使用 显示为空
           /* InputStream inputStream1 = source.buffer().clone().inputStream();
            //InputStream inputStream2 = response.body().byteStream(); 导致Retrofit解析不到body  此方法只可调用一次 okhttp防止占用内存会清楚缓存
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream1);
            StringBuffer resultBuffer = new StringBuffer();
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                resultBuffer.append(temp);
            }*/
            Gson gson = new Gson();
            LogUtils.d("返回aj请求信息" + resultBuffer);
            final BaseResponse baseResponse = gson.fromJson(resultBuffer, BaseResponse.class);
        }
        return response;
    }
}


