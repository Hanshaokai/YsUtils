package zity.net.basecommonmodule.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;


/**
 * author：hanshaokai
 * date： 2018/9/7 0007 9:32
 * describe： px=dp*density dp是360dp（设计效果图宽度） 使得px正好是屏幕宽度 修改density
 */

public class DensityUtils {
    // 适配修改设备密度
    private static float sNoncompatDensity;
    private static float sNoncompatScaledDensity;

    public static void setCustomDensity(@NonNull Activity activity, @NonNull final Application application) {
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (sNoncompatDensity == 0) {
            sNoncompatDensity = appDisplayMetrics.density;
            sNoncompatScaledDensity = appDisplayMetrics.scaledDensity;
            //防止系统切换后不起作用
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null & newConfig.fontScale > 0) {
                        sNoncompatScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
        float targetDensity = appDisplayMetrics.widthPixels / 360;
        //防止字体变小
        float targetScaleDensity = targetDensity * (sNoncompatScaledDensity / sNoncompatDensity);
        int targetDensityDpi = (int) (160 * targetDensity);
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaleDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;

        final DisplayMetrics activityDisplayMetrice = activity.getResources().getDisplayMetrics();
        activityDisplayMetrice.density = targetDensity;
        activityDisplayMetrice.scaledDensity = targetScaleDensity;
        activityDisplayMetrice.densityDpi = targetDensityDpi;

    }

//----------------------------------百度人脸检测工具
    /**
     * 四舍五入
     */
    private static final float DOT_FIVE = 0.5f;
    /**
     * portrait degree:90
     */
    private static final int PORTRAIT_DEGREE_90 = 90;

    /**
     * portrait degree:270
     */
    private static final int PORTRAIT_DEGREE_270 = 270;

    /**
     * Private constructor to prohibit nonsense instance creation.
     */
    private DensityUtils() {
    }

    /**
     * sp转px.
     *
     * @param context context
     * @param spValue spValue
     * @return 换算后的px值
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * dip转换成px
     *
     * @param context Context
     * @param dip     dip Value
     * @return 换算后的px值
     */
    public static int dip2px(Context context, float dip) {
        float density = getDensity(context);
        return (int) (dip * density + DensityUtils.DOT_FIVE);
    }

    /**
     * px转换成dip
     *
     * @param context Context
     * @param px      px Value
     * @return 换算后的dip值
     */
    public static int px2dip(Context context, float px) {
        float density = getDensity(context);
        return (int) (px / density + DOT_FIVE);
    }

    /**
     * 得到显示宽度
     *
     * @param context Context
     * @return 宽度
     */
    public static int getDisplayWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到显示高度
     *
     * @param context Context
     * @return 高度
     */
    public static int getDisplayHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到显示密度
     *
     * @param context Context
     * @return 密度
     */
    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 得到DPI
     *
     * @param context Context
     * @return DPI
     */
    public static int getDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * 判断当前Android系统能否竖着屏幕取扫描二维码 2.1版本的ROM是不支持的竖屏扫描的，而且发现过一台三星-GT-S5830i也不支持竖屏扫描
     *
     * @return 当前Android系统能竖着屏幕取扫描二维码：true, 不能：false
     */
    public static boolean supportCameraPortrait() {
        return APIUtils.hasFroyo()
                && !TextUtils.equals("GT-S5830i", Build.PRODUCT);
    }

    /**
     * 判断当前Android系统摄像头旋转多少度
     *
     * @return 当前Android系统能竖着屏幕，
     * 正常应该旋转90度，
     * 但是斐讯i700v、夏新A862W、桑菲V8526需要旋转270度
     */
    public static int getPortraitDegree() {
        int degree = PORTRAIT_DEGREE_90;
        // 为了更好的扩展更多的特殊设置型号，将要比较的设备型号提成一个数组，遍历这个数据。
        for (String model : BUILD_MODELS) {
            if (TextUtils.equals(model, Build.MODEL)) {
                degree = PORTRAIT_DEGREE_270;
                break;
            }
        }
        return degree;
    }

    /**
     * 需要比较的设置型号
     */
    private static final String[] BUILD_MODELS = {
            "i700v", //斐讯i700v
            "A862W", //夏新A862W
            "V8526"  //桑菲V8526
    };

}
