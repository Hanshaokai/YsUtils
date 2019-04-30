package zity.net.basecommonmodule.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.net.config.NetConfigProvider;
import zity.net.basecommonmodule.net.config.NetConfigProviderImpl;
import zity.net.basecommonmodule.net.db.GreenDaoManager;
import zity.net.basecommonmodule.net.download.DownInfo;
import zity.net.basecommonmodule.net.download.DownStateEnum;
import zity.net.basecommonmodule.net.download.ProgressDownSubscriber;
import zity.net.basecommonmodule.net.exception.HttpTimeException;
import zity.net.basecommonmodule.net.intercepte.AnJianHandlerImpl;
import zity.net.basecommonmodule.net.intercepte.DefaultHandlerImpl;
import zity.net.basecommonmodule.net.intercepte.DownLoadInterceptor;
import zity.net.basecommonmodule.net.intercepte.RequestInterceptor;
import zity.net.basecommonmodule.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 15:22
 * describe：
 */


public class RxManager {

    private static RxManager instance;
    private final long connectTimeoutMills = 8 * 1000L;
    private final long readTimeoutMills = 10 * 1000L;
    private NetConfigProvider sProvider = null;
    private Map<String, NetConfigProvider> providerMap = new HashMap<>();
    private Map<String, Retrofit> retrofitMap = new HashMap<>();
    private Map<String, OkHttpClient> clientMap = new HashMap<>();

    // 记录下载数据
    private Set<DownInfo> downInfos;
    //回调 队列
    private HashMap<String, ProgressDownSubscriber> subMap;
    //数据库类
    private GreenDaoManager db;

    private RxManager() {
        downInfos = new HashSet<>();
        subMap = new HashMap<>();
        db = GreenDaoManager.getInstance();
    }

    /**
     * 请求地址不同或者请求链接属性不同 另外定义方法
     * 方法泛型
     *
     * @return
     */
    public static <T> T getService(Class<T> t) {
        return RxManager.defaultClient(t).create(t);
    }

    /**
     * 得到配置好的retrofit
     */

    public static Retrofit defaultClient(Class t) {
        return RxManager.getInstance().getRetrofit(Constants.
                BASE_URL, t);
    }

    public Retrofit getRetrofit(String baseUrl, Class t) {
        NetConfigProviderImpl netConfigProvider = new NetConfigProviderImpl();
        //默认
        LogUtils.d("服务名字" + t.getSimpleName());
        if (t.getSimpleName().equals("ShortService")) {
            netConfigProvider.setCONNECT_TIME_OUT(5 * 1000L,5000L,5000L);
            return getRetrofit(baseUrl, t, netConfigProvider);
        }
        return getRetrofit(baseUrl, t, null);
    }

    public static RxManager getInstance() {
        if (instance == null) {
            synchronized (RxManager.class) {
                if (instance == null) {
                    instance = new RxManager();
                }
            }
        }
        return instance;
    }

    public Retrofit getRetrofit(String baseUrl, Class t, NetConfigProvider provider) {
        if (empty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (retrofitMap.get(t.getSimpleName()) != null) {
            return retrofitMap.get(t.getSimpleName());
        }

        if (provider == null) {
            provider = providerMap.get(t.getSimpleName());
            if (provider == null) {
                // provider 可以针对不同请求设置 不同请求参数 不同拦截器 这里暂时默认一种
                if (sProvider == null) {
                    sProvider = new NetConfigProviderImpl();
                }
                provider = sProvider;
            }
        }
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, t, provider))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit = builder.build();
        retrofitMap.put(t.getSimpleName(), retrofit);
        providerMap.put(t.getSimpleName(), provider);
        return retrofit;
    }

    private boolean empty(String baseUrl) {
        return baseUrl == null || baseUrl.isEmpty();
    }

    /**
     * @param baseUrl
     * @param t        class 类
     * @param provider 请求 的参数提供者
     * @return
     */
    private OkHttpClient getClient(String baseUrl, Class t, NetConfigProvider provider) {
        if (empty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (clientMap.get(t.getSimpleName()) != null) {
            return clientMap.get(t.getSimpleName());
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //设置超时时间 读写时间
        builder.connectTimeout(provider.configConnectTimeoutSecs() != 0
                ? provider.configConnectTimeoutSecs()
                : connectTimeoutMills, TimeUnit.MILLISECONDS);
        builder.readTimeout(provider.configReadTimeoutSecs() != 0
                ? provider.configReadTimeoutSecs() : readTimeoutMills, TimeUnit.MILLISECONDS);

        builder.writeTimeout(provider.configWriteTimeoutSecs() != 0
                ? provider.configReadTimeoutSecs() : readTimeoutMills, TimeUnit.MILLISECONDS);
        CookieJar cookieJar = provider.configCookie();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        provider.configHttps(builder);
        LogUtils.d(t.getSimpleName() + "安监类名");
        if (!t.getSimpleName().equals("AnjianService"))// 在此处定义的请求 不拦截
        {
            builder.addInterceptor(new RequestInterceptor(new DefaultHandlerImpl()));
        } else {
            builder.addInterceptor(new RequestInterceptor(new AnJianHandlerImpl()));
        }
        //如果需要针对某个请求加拦截 则在provider 中定义
        Interceptor[] interceptors = provider.configInterceptors();
        if (!empty(interceptors)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (provider.configLogEnable()) {
            //添加日志拦截器 日志处理的地方   可以重载构造方法 自定义的logger
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient client = builder.build();
        clientMap.put(t.getSimpleName(), client);
        providerMap.put(t.getSimpleName(), provider);

        return client;
    }

    private void checkProvider(NetConfigProvider provider) {
        if (provider == null) {
            throw new IllegalStateException("must register provider first");
        }
    }

    private boolean empty(Interceptor[] interceptors) {
        return interceptors == null || interceptors.length == 0;
    }

    public static HttpDownService getDownLoadService() {
        return RxManager.defaultClient(HttpDownService.class).create(HttpDownService.class);
    }

    public <S> S get(String baseUrl, Class<S> service) {
        return getInstance().getRetrofit(baseUrl, service).create(service);
    }

    public void registerProvider(NetConfigProvider provider) {
        this.sProvider = provider;
    }

    public void registerProvider(String baseUrl, NetConfigProvider provider) {
        getInstance().providerMap.put(baseUrl, provider);
    }

    public NetConfigProvider getCommonProvider() {
        return sProvider;
    }

    public void clearCache() {
        getInstance().retrofitMap.clear();
        getInstance().clientMap.clear();
    }

    public Map<String, Retrofit> getRetrofitMap() {
        return retrofitMap;
    }

    public Map<String, OkHttpClient> getClientMap() {
        return clientMap;
    }

    /**
     * 停止下载
     */
    public void stopDown(DownInfo info) {
        if (info == null) {
            return;
        }
        info.setState(DownStateEnum.STOP);
        info.getListener().onStop();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.dispose();
            subMap.remove(info.getUrl());
        }
        /*保存数据库信息和本地文件*/
        db.save(info);
    }

    /*停止全部下载*/
    public void stopAllDown() {
        for (DownInfo downInfo : downInfos) {
            startDown(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /**
     * 开始下载
     */
    public void startDown(final DownInfo info) {
        //正在下载不处理
        if (info == null || subMap.get(info.getUrl()) != null) {
            subMap.get(info.getUrl()).setDownInfo(info);
            return;
        }
        //添加回调处理类
        ProgressDownSubscriber subscriber = new ProgressDownSubscriber(info);
        //记录回调sub
        subMap.put(info.getUrl(), subscriber);
        //获取service 多次请求公用一个service
        HttpDownService httpDownService;
        if (downInfos.contains(info)) {
            httpDownService = info.getService();
        } else {
            DownLoadInterceptor interceptor = new DownLoadInterceptor(subscriber);
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            //手动创建一个OKHttpClient 并设置超时时间
            builder.connectTimeout(info.getConnectonTime(), TimeUnit.MILLISECONDS);
            builder.addInterceptor(interceptor);

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .build();
            httpDownService = retrofit.create(HttpDownService.class);
            info.setService(httpDownService);
            downInfos.add(info);
        }
        //得到rx对象 上一次下载的位置开始下载
        //getUrl 得到书名 带 文件扩展名  后台暂不支持断点下载
        /*  httpDownService.downLoadFile("bytes=" + info.getReadLength() + "-", info.getUrl())
         *//*指定线程*//*
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
        *//*失败后的retry 配置*//*
                .retryWhen(new RetryWhenNetWorkException())
        *//*读取下载写入文件*//*
                .map(new Func1<ResponseBody, DownInfo>() {
                    @Override
                    public DownInfo call(ResponseBody responseBody) {

                        writeCaches(responseBody, new File(info.getSavePath()), info);
                        return info;
                    }
                })
                *//*回调线程*//*
                .observeOn(AndroidSchedulers.mainThread())
                 *//*数据回调*//*
                .subscribe(subscriber);*/
    }

    public void writeCaches(ResponseBody responseBody, File file, DownInfo info) {
        try {
            RandomAccessFile randomAccessFile = null;
            FileChannel channelOut = null;
            InputStream inputStream = null;

            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            long allLength = 0 == info.getCountLength() ? responseBody.contentLength()
                    : info.getReadLength() + responseBody.contentLength();
            inputStream = responseBody.byteStream();
            randomAccessFile = new RandomAccessFile(file, "rwd");
            channelOut = randomAccessFile.getChannel();
            MappedByteBuffer mappedByteBuffer = channelOut.map(FileChannel.MapMode.READ_WRITE
                    , info.getReadLength(), allLength - info.getReadLength());
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                mappedByteBuffer.put(buffer, 0, len);
            }

        } catch (IOException e) {
            throw new HttpTimeException(e.getMessage());
        }
    }

    /*暂停全部下载*/

    public void pauseAll() {
        for (DownInfo downInfo : downInfos) {
            pause(downInfo);
        }
        subMap.clear();
        downInfos.clear();
    }

    /*暂停下载*/
    public void pause(DownInfo info) {
        if (info == null) {
            return;
        }
        info.setState(DownStateEnum.PAUSE);
        info.getListener().onPuase();
        if (subMap.containsKey(info.getUrl())) {
            ProgressDownSubscriber subscriber = subMap.get(info.getUrl());
            subscriber.dispose();
            subMap.remove(info.getUrl());
        }
        /*这里需要讲info 信息写入到数据中可自由扩展 用自己项目的数据库*/
        db.update(info);
    }

    /*返回全部正在下载的数据*/
    public Set<DownInfo> getDownInfos() {
        return downInfos;
    }

    /*移除下载数据*/
    public void remove(DownInfo info) {
        subMap.remove(info.getUrl());
        downInfos.remove(info);
    }


}
