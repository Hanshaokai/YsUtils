package zity.net.basecommonmodule.net.intercepte;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Looper;
import androidx.appcompat.app.AlertDialog;

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
import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.commonbase.BaseResponse;
import zity.net.basecommonmodule.net.exception.ApiException;
import zity.net.basecommonmodule.utils.ActivityUtils;
import zity.net.basecommonmodule.utils.LogUtils;
import zity.net.basecommonmodule.utils.NetworkUtils;
import zity.net.basecommonmodule.utils.ToastUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 16:43
 * describe：
 */


public class DefaultHandlerImpl implements RequestHandler {
    private static final android.os.Handler HANDLER = new android.os.Handler(Looper.getMainLooper());

    @Override
    public Request onBeforeRequest(Request request, final Interceptor.Chain chain) throws UnsupportedOperationException {
        //请求前判断网络
        if (!NetworkUtils.isConnected()) {
            ActivityUtils.getTopActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!chain.request().url().uri().getPath().contains("/app/user/updateVersion")) {
// 欢迎页面以弹窗提示
                        ToastUtils.showShort("网络未连接");
                    }
                }
            });
            throw new UnsupportedOperationException("请检查网络是否连接");
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
            throw new UnsupportedOperationException("链接错误");
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
            LogUtils.d("返回请求信息" + resultBuffer);
            final BaseResponse baseResponse = gson.fromJson(resultBuffer, BaseResponse.class);
        /*    inputStream1.close();
            bufferedReader.close();
            inputStreamReader.close();*/
            /*BaseResponse{success=true, api_version=0, code=2000, total=-1, user_msg='登录成功!', server_msg=''}*/
            if (!baseResponse.getSuccess()) {
                // 第一级判断是否服务器错误
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("server_error");
                    }
                });
                throw new UnsupportedOperationException("服务器错误");
                //服务器错误状态码返回给显示界面
            } else {
                // 第二级判断错误状态码 即非2000的情况 3000 情况需要补充信息
                if (baseResponse.getCode() != Constants.REQUEST_SUCCESS && baseResponse.getCode() != 3000) {
                    if (activity != null && !activity.isFinishing()) {
                        switch (baseResponse.getCode()) {
                            case Constants.TOKEN_ERROR_CODE:
                                // 更改UI需要在主线程
                                // token 异常情况处理
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                        AlertDialog dialog = builder.setMessage("您的账号已在其他设备上登录，点击确定重新登录")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                    }
                                                }).create();
                                        dialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
                                        dialog.show();
                                    }
                                });
                                break;
                            case Constants.API_VERSION_NOT_SUPPOSED_CODE:   // 数据版本不一致情况
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlertDialog.Builder versionBuild = new AlertDialog.Builder(activity);
                                        AlertDialog versionDialog = versionBuild.setMessage("您的当前版本过旧，点击确定下载最新版本")
                                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                    }
                                                }).create();
                                        versionDialog.setCanceledOnTouchOutside(false);//点击屏幕不消失
                                        versionDialog.show();
                                    }
                                });
                                break;
                            case Constants.LOGIN_ERROR://登录失败
                            case Constants.PASSWORD_CORRECT_ERROR://修改密码失败
                              /*  activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    // 提示内容下放
                                        if (!TextUtils.isEmpty(baseResponse.getUser_msg())) {
                                            ToastUtils.showShort(baseResponse.getUser_msg());
                                        }
                                    }
                                });*/
                                break;
                            default:
                        }
                    }
                    // code值非2000 抛出异常 走错误回调
                    throw new UnsupportedOperationException(baseResponse.getUser_msg());
                }
            }
        }
        return response;
    }
}

