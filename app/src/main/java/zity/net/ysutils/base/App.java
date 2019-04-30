package zity.net.ysutils.base;

import zity.net.basecommonmodule.app.BaseCommonApp;

/**
 * author：hanshaokai
 * date： 2019/4/25 0025 18:45
 * describe：
 */

public class App extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    protected void initLogic() {
        registerApplicationLogic(BaseCommonApp.class);
    }
}
