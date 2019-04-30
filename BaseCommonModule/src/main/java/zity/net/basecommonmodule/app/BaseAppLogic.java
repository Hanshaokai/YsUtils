package zity.net.basecommonmodule.app;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import androidx.annotation.NonNull;

/**
 * author：hanshaokai
 * date： 2018/11/29 0029 9:43
 * describe： 通过 在主module 的Application中继承 Base module 的Application 来实现。主module 的Application 将注册每个module
 * 的初始化文件 ，然后通过Base module中的Application 来对初始化文件做启动封装
 */

public class BaseAppLogic {
    protected static Application mApplication;
    public static String flavorName;

    public BaseAppLogic() {
    }


    public void setApplication(@NonNull Application application) {
        mApplication = application;
    }

    public static Application getAppClication() {
        return mApplication;
    }

    public void onCreate() {
    }

    public void onTerminate() {
    }

    protected void attachBaseContext(Context base) {

    }

    public void onLowMemory() {
    }

    public void onTrimMemory(int leve) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
    }

    public void setFlavorName(@NonNull String flavorName) {
        this.flavorName = TextUtils.isEmpty(flavorName) ? "ys_utils" : flavorName;
    }
}
