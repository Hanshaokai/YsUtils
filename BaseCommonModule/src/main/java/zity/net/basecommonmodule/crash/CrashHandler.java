package zity.net.basecommonmodule.crash;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import zity.net.basecommonmodule.BuildConfig;
import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.utils.ActivityUtils;
import zity.net.basecommonmodule.utils.LogUtils;
import zity.net.basecommonmodule.utils.TimeUtils;
import zity.net.basecommonmodule.utils.ToastUtils;

/**
 * author：hanshaokai
 * date： 2018/5/29 19:34
 * describe：
 */


public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final int MAX_STACK_TRACE_SIZE = 131071;
    private static final String TAG = "CrashHandler";
    private static final boolean DEBUG = true;
    private static final String FILE_NAME = "crash";
    private static final String FILE_NAME_SUFFIX = ".txt";
    private static String PATH;
    private static CrashHandler sInstance = null;
    //系统默认的异常处理（默认情况下，系统会终止当前的异常程序）
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private Context mContext;
    private File file;

    private CrashHandler(Context cxt) {
        file = new File(PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
        init(cxt);
    }

    //这里主要完成初始化工作
    public void init(Context context) {
        //获取系统默认的异常处理器
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        //将当前实例设为系统默认的异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    public synchronized static CrashHandler create(Context cxt) {
        if (sInstance == null) {
            PATH = Constants.FILE_PATH_DOWNLOAD + "/error_log/";
            sInstance = new CrashHandler(cxt);
        }
        return sInstance;
    }

    /**
     * 当程序中有未被捕获的异常，系统将会自动调用#uncaughtException方法
     * thread为出现未捕获异常的线程，ex为未捕获的异常。
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtils.d("uncaughtException", thread.getName() + "--Exception:" + ex + "\n\n");
        if (!handleException(ex) && mDefaultCrashHandler != null) {
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultCrashHandler.uncaughtException(thread, ex);
        } else {
            switch (BuildConfig.BUILD_TYPE) {
                case "debug":
                    //在调试窗显示错误信息  没有这句话不再显示  debug 版运行出错打印错误日志不弹出窗口
                    mDefaultCrashHandler.uncaughtException(thread, ex);
                    break;
                //测试走这里 (无此类型)
                case "beta":
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                    }
                    //处理异常
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    //导出发生异常的时间
                    //      pw.println("Build_time: " + BuildConfig.RELEASETIME);
                    pw.println("error_time: " + TimeUtils.getNowString());
                    //导出手机信息
                    try {
                        dumpPhoneInfo(pw);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    pw.println();
                    //导出异常的调用栈信息
                    ex.printStackTrace(pw);
                    String stackTraceString = sw.toString();
                    pw.close();
                    if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
                        String disclaimer = " [stack trace too large]";
                        stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
                    }
                    //测试版本崩溃弹出 错误日志
                  /*  Intent intentapp = new Intent(mContext, ErrorActivity.class);
                    intentapp.putExtra("error", stackTraceString);
                    intentapp.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intentapp);*/
                    ActivityUtils.appExit();
                    break;
                //外网测试正式打包走这里
                default:
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                    }
                    ActivityUtils.appExit();
            }
        }
    }

    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() { ToastUtils.showShort("程序出现异常即将退出...");
                    }
                });
            }
        }.start();
        dumpExceptionToSDCard(ex);
        return !BuildConfig.DEBUG;
    }

    private void dumpPhoneInfo(PrintWriter pw) throws PackageManager.NameNotFoundException {
        //应用的版本名称和版本号
        PackageManager pm = mContext.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
       /* //版本名
        pw.print(" AppCg Version_Name: ");
        pw.println(pi.versionName);
        //版本号
        pw.print(" AppCg Version_Code: ");
        pw.println(pi.versionCode);*/

        //android版本号
        pw.print(" OS Version: ");
        pw.println(Build.VERSION.RELEASE);
        //android sdk int
      /*  pw.print(" SDK Version:");
        pw.println(Build.VERSION.SDK_INT);*/

       /* //手机制造商
        pw.print("Vendor: ");
        pw.println(Build.MANUFACTURER);*/

        //手机型号
        pw.print("Model: ");
        pw.println(Build.MODEL);

       /* //cpu架构
        pw.print("CPU ABI: ");
        if (Build.VERSION.SDK_INT >= 21) {
            for (int i = 0; i < Build.SUPPORTED_ABIS.length; i++) {
                pw.println(Build.SUPPORTED_ABIS[i]);
            }

        }*/
    }

    private void dumpExceptionToSDCard(Throwable ex) {
        //如果SD卡不存在或无法使用，则无法把异常信息写入SD卡
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (DEBUG) {
                Log.w(TAG, "sdcard unmounted,skip dump exception");
                return;
            }
        }
        long current = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(current));
        @SuppressLint("SimpleDateFormat") String time1 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(current));
        //以当前时间创建log文件
        File file1 = new File(file, FILE_NAME + time + FILE_NAME_SUFFIX);
        try {
            StringBuffer sb = new StringBuffer();
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            /*  printWriter.println("Build_time: " + BuildConfig.RELEASETIME);*/
            printWriter.println("error_time: " + time1);
            //导出手机信息
            dumpPhoneInfo(printWriter);
            printWriter.println();
            //导出异常的调用栈信息
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            printWriter.close();
            String result = writer.toString();
            sb.append(result);
            FileOutputStream fos = new FileOutputStream(file1);
            fos.write(sb.toString().getBytes());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "dump crash info failed : " + e.getMessage());
        }
    }

    /**
     * 此处上传到服务器错误日志
     */
    private void uploadExceptionToServer(Throwable tb) {
        //TODO Upload Exception Message To Your Web Server
        String errorMsg = tb.getMessage();
    }


}
