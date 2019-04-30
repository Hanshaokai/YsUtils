package zity.net.basecommonmodule.app;

import org.jetbrains.annotations.NotNull;

import java.io.File;

import io.reactivex.Maybe;
import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;
import zity.net.basecommonmodule.BuildConfig;
import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.crash.CrashHandler;
import zity.net.basecommonmodule.utils.LogUtils;
import zity.net.basecommonmodule.utils.Utils;
import zlc.season.rxdownload3.core.DownloadConfig;
import zlc.season.rxdownload3.core.Normal;
import zlc.season.rxdownload3.core.RealMission;
import zlc.season.rxdownload3.extension.Extension;

/**
 * author：hanshaokai
 * date： 2018/12/4 0004 16:23
 * describe： 在各自组件内的 Application中初始化
 */

public class BaseCommonApp extends BaseAppLogic {

    @Override
    public void onCreate() {
        super.onCreate();
        //本地工具类注册上下文 日志开关
        Utils.init(mApplication);
        LogUtils.getConfig().setLogSwitch(BuildConfig.DEBUG);
        //本地类错误日志处理初始化
        CrashHandler.create(mApplication);
        //第三方RxJavaDownload参数配置
        DownloadConfig.Builder builder = DownloadConfig.Builder.Companion.create(mApplication)
                .setDefaultPath(Constants.FILE_PATH_DOWNLOAD)     //Set the default download address
                .enableDb(false)     //Enable the database
                // .setDbActor(CustomSqliteActor(this))    //Customize the database
                //.enableService(true)    //Enable Service
                // .useHeadMethod(true)    //Use http HEAD method.
                // .setMaxRange(10)       // Maximum concurrency for each mission.
                .setRangeDownloadSize(4 * 1000 * 1000) //The size of each Range，unit byte
                .setMaxMission(1)      // The number of mission downloaded at the same time
                .enableNotification(false);//Enable Notification
        // .setNotificationFactory(NotificationFactoryImpl())      //Custom notification
        //.setOkHttpClientFacotry(OkHttpClientFactoryImpl())      //Custom OKHTTP
        // .addExtension(CustomExtension.class);  //Add extension
        DownloadConfig.INSTANCE.init(builder);

    }

    //-----------Rxdowonlod 扩展 解决删除url 仍是 成功标识问题 待确认
    public static class CustomExtension implements Extension {
        private RealMission mRealMission;

        @Override
        public void init(RealMission realMission) {
            mRealMission = realMission;
        }

        @NotNull
        @Override
        public Maybe<Object> action() {
            return Maybe.create(new MaybeOnSubscribe<Object>() {
                @Override
                public void subscribe(MaybeEmitter<Object> e) throws Exception {
                    File file = mRealMission.getFile();

                    if (file == null || !file.exists()) {
                        mRealMission.emitStatus(new Normal(mRealMission.getStatus()));
                        e.onComplete();
                        return;
                    }
                    e.onSuccess(file);
                }
            });
        }
    }

}
