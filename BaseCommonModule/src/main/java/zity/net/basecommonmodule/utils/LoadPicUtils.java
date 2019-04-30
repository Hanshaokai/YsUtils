package zity.net.basecommonmodule.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import zity.net.basecommonmodule.Constants;
import zity.net.basecommonmodule.R;

/**
 * author：hanshaokai
 * date： 2018/7/6 0006 10:53
 * describe：
 */

public class LoadPicUtils {
    /*public static void loadPicToPhotoView(final Context context, final ImageView photoView, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.bga_pp_ic_holder_light)    //加载成功之前占位图
                .error(R.mipmap.bga_pp_ic_holder_light)    //加载错误之后的错误图
              //  .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)    //指定图片的尺寸  这里下载原图
                // 指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                // .fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取  中间的显示。）
                 .centerCrop()
                // .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                // .skipMemoryCache(true)    //跳过内存缓存
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)    //缓存所有版本的图像
                // .diskCacheStrategy(DiskCacheStrategy.NONE)    //跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
                //  .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存最终的图片
                ;
        Glide.with(context).load(Constants.BASE_URL + url).apply(options).into(photoView);
    }*/

    public static void loadAvatar(final Context context, final ImageView photoView, String url) {
        RequestOptions options = new RequestOptions()
                //   .placeholder(R.mipmap.ic_launcher)    //加载成功之前占位图
                //.error(R.mipmap.ic_launcher)
                .circleCrop();  //加载错误之后的错误图
        Glide.with(context).load(Constants.BASE_URL + url).apply(options).into(photoView);
        //  .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存最终的图片
    }

    public static void loadPicToPhotoView(final Context context, final ImageView photoView, String url) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.mipmap.bga_pp_ic_holder_light)    //加载成功之前占位图
                .error(R.mipmap.bga_pp_ic_holder_light)    //加载错误之后的错误图
                //  .override(com.bumptech.glide.request.target.Target.SIZE_ORIGINAL)    //指定图片的尺寸  这里下载原图
                // 指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                // .fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取  中间的显示。）
                .centerCrop()
                // .circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                // .skipMemoryCache(true)    //跳过内存缓存
                //  .diskCacheStrategy(DiskCacheStrategy.ALL)    //缓存所有版本的图像
                // .diskCacheStrategy(DiskCacheStrategy.NONE)    //跳过磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
                //  .diskCacheStrategy(DiskCacheStrategy.RESOURCE)  //只缓存最终的图片
                ;
        Glide.with(context).load(Constants.BASE_URL + url).apply(options).into(photoView);
    }
}
