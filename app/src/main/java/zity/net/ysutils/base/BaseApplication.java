package zity.net.ysutils.base;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import zity.net.basecommonmodule.app.BaseAppLogic;
import zity.net.ysutils.BuildConfig;

/**
 * author：hanshaokai
 * date： 2018/11/29 0029 9:44
 * describe： Application模板模式
 */

public abstract class BaseApplication extends Application {
    private List<Class<? extends BaseAppLogic>> logicList = new ArrayList<>();
    private List<BaseAppLogic> logicClassList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        initLogic();
        logicCreate();
        for (BaseAppLogic logic : logicClassList) {
            logic.onCreate();
        }
    }


    /**
     * 主module 的 Application 调用
     */
    protected abstract void initLogic();

    protected void registerApplicationLogic(Class<? extends BaseAppLogic> logicClass) {
        logicList.add(logicClass);
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        for (BaseAppLogic logic : logicClassList) {
            logic.onTerminate();
        }
    }

    private void logicCreate() {
        for (Class<? extends BaseAppLogic> logicClass : logicList) {
            try {
                //使用反射初始化调用
                BaseAppLogic appLogic = logicClass.newInstance();
                appLogic.setApplication(this);
                appLogic.setFlavorName(BuildConfig.FLAVOR);
                logicClassList.add(appLogic);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
