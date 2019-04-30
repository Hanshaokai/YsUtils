package zity.net.basecommonmodule.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * author：hanshaokai
 * date： 2018/6/28 0028 15:07
 * describe：
 */

public class GlideUtils {
    public GlideUtils() {
        throw new UnsupportedOperationException("can not instantiate");
    }

    public static void loadModuleImg(String url, ImageView imageView, Context context) {
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(context).asBitmap().load(url).into(imageView);
    }


    public static void loadImg(String url, ImageView imageView, Context context) {
        Glide.with(context).load(url).into(imageView);
    }

    public static void loadNativeImg(Context context, Drawable drawable, ImageView imageView) {
        Glide.with(context).load(drawable).into(imageView);
    }

    public static void loadNativeCircleImg(Context context, Drawable drawable, ImageView imageView) {
        RequestOptions requestOptions=new RequestOptions();
        RequestOptions options = requestOptions.circleCrop();
        Glide.with(context).load(drawable).apply(options).into(imageView);
    }

    public static void loadNaticeUrlImg(Context context, String url, ImageView imageView) {
        RequestOptions options = new RequestOptions().centerCrop();
        Glide.with(context).load(new File(url)).apply(options).into(imageView);
    }
}
