package zity.net.ysutils.keepalive;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.util.List;

import zity.net.aidl.ICat;
import zity.net.ysutils.utils.LogUtils;

/**
 * author：hanshaokai
 * date： 2019/4/26 0026 15:56
 * describe：保活后台服务，绑定启动保活助手A的服务
 * YS打开保活助手A  A打开保活助手B B打开YS和保活助手A 华为9.0上无法实现关联启动
 */

public class LcbAliveService extends Service {
    private final String A_PackageName = "zity.net.helpalivea";
    private final String A_ServicePath = "zity.net.helpalivea.AssistantAService";
    private ICat mBinderFromA;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderFromA = ICat.Stub.asInterface(service);
            if (mBinderFromA != null) {
                try {
                    LogUtils.writeLog("收到保活助手A的数据：name="
                            + mBinderFromA.getName());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.writeLog("收到保活助手断开");
            // 收到了断开 但在启动时没起作用
            bindAliveA();
        }
    };
    private ICat.Stub mBinderToA = new ICat.Stub() {
        @Override
        public String getName() throws RemoteException {
            return "我是被保活的";
        }

        @Override
        public int getAge() throws RemoteException {
            return 3;
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinderToA;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isApkInstalled(A_PackageName)) {
            LogUtils.writeLog("保活助手A未安装");
            stopSelf();// 非绑定式启动service后被启动的service将不受访问者控制，也无法与访问者通信，会无限的运行下去
            // 必须调用stopSelf或者有组件调用stopService来停止 onCreate 方法只会被调用一次
            // onStartCommand会被调用多次onDestroy销毁时只调用一次
            // bindService 访问者将通过一个IBinder接口的对象与被绑定Service进行通信，通过unBindService方法随时关闭service
            //一个service可以同时被多个访问者绑定只有当多个访问者都主动解除绑定才会销毁。 onCreate 只会初始创建才调用，onbind  unOnbind 跟绑定次相同
            //onBind 用于返回一个通信对象（Ibinder）给访问者访者通过该IBinder对象调用service的相关方法
            // 绑定一个非绑定启动的服务，解绑后不会销毁，只有调用stopSelf或者有组件调用stopService销毁
            return;
        }
        bindAliveA();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;// 使系统内存足够的时候Service能够自启动
    }

    private void bindAliveA() {
        Intent serverIntent = new Intent();
        serverIntent.setClassName(A_PackageName, A_ServicePath);
        bindService(serverIntent, conn, Context.BIND_AUTO_CREATE);
    }

    private boolean isApkInstalled(String packageName) {
        PackageManager manager = getPackageManager();
        // 获得所有已经安装的包信息
        List<PackageInfo> infos = manager.getInstalledPackages(0);
        for (PackageInfo info : infos) {
            LogUtils.writeLog(info.packageName);
            LogUtils.writeLog(info.sharedUserId);
            LogUtils.writeLog(info.applicationInfo.name);
        }
        for (PackageInfo info : infos) {
            if (info.packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }
}



