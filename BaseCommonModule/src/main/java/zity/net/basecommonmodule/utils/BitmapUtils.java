package zity.net.basecommonmodule.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author hanshaokai
 * @date 2018/5/2 17:30
 * describe
 */


public class BitmapUtils {


    /*   @Deprecated
       public static Bitmap getRoundCornerImage(Bitmap bitmap, int roundPixels) {
       *//*1.Bitmap.Config ALPHA_8
    这个参数每个像素占用1字节的空间。
    它代表每个像素点被存储为单个透明度的通道，这对于设置遮罩的图片用例十分有用，它不存储颜色信息。


    2.Bitmap.Config RGB_565
    这个参数每个像素占用2字节的空间。
    它代表只有RGB通道的编码，其中红色占用5位地址，绿色占用6位地址，蓝色占用5位地址。没有透明度的通道。
    使用不透明的位图时，不要求高的色彩保真度使用此配置是不错的选择。


    3.Bitmap.Config ARGB_4444
    这个参数每个像素占用2字节的空间。
    它一共有四个通道，顾名思义，分别是透明度、红、绿、蓝。每个通道分别占用四位地址，所以一共2字节。
    当应用需要节省内存(对色彩质量要求低)，同时又需要存储透明度信息，这个配置可以作为选择，但官方比较推荐用ARGB_8888的设置，因为这个的色彩质量差。


    4.Bitmap.Config ARGB_8888
    这个参数每个像素占用4字节的空间。

    -这也是一共4个通道，但不一样的是每个通道站8位地址，因而色彩质量比上一个设置高了特别特别多（16倍）。 *//*
        Bitmap roundConerImage = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundConerImage);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return roundConerImage;
    }

    @Deprecated
    private static void addInBitmapOptions(BitmapFactory.Options options) {
// inBitmap 只处理可变的位图 所以强制返回可变的位图
        options.inMutable = true;
    }

    /**
     * 图片的二次采样
     *
     * @param res
     * @param resId
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    @Deprecated
    public static Bitmap decodeBitmapFromResource(Resources res, int resId
            , int reqWidth, int reqHeight) {
        // 首先通过inJustDecodeBounds=true 获得图片的尺寸
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        //根据图片分辨率以及我们实际需要展示的大小 计算压缩率
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    //返回缩放比 根据要求宽高
    @Deprecated
    private static int calculateInSampleSize(BitmapFactory.Options options
            , int reqWidth
            , int reqHeight
    ) {
        int outH = options.outHeight;
        int outW = options.outWidth;
        int intSampleSize = 1;
        while (outH / intSampleSize > reqHeight || outW / intSampleSize > reqWidth) {
            intSampleSize *= 2;
        }
        options.inSampleSize = intSampleSize;
        return options.inSampleSize;
    }


    /**
     * 按图片尺寸压缩 参数是bitmap
     *
     * @param bitmap
     * @param pixelW
     * @param pixelH
     * @return
     */
    public static Bitmap compressImageFromBitmap(Bitmap bitmap, int pixelW, int pixelH) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
        if (os.toByteArray().length / 1024 > 512) {//判断如果图片大于0.5M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            os.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os);//这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeStream(is, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = computeSampleSize(options, pixelH > pixelW ? pixelW : pixelH, pixelW * pixelH);
        is = new ByteArrayInputStream(os.toByteArray());
        Bitmap newBitmap = BitmapFactory.decodeStream(is, null, options);
        return newBitmap;
    }

    /**
     * 根据路径获得图片信息并按比例压缩，返回bitmap
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只解析图片边沿，获取宽高
        BitmapFactory.decodeFile(filePath, options);
        // 计算缩放比
        options.inSampleSize = calculateFileInSampleSize(options, 480, 800);
        // 完整解析图片返回bitmap
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static String compressImage(String filePath, String targetPath, int quality) {
        Bitmap bm = getSmallBitmap(filePath);//获取一定尺寸的图片
        int degree = readPictureDegree(filePath);//获取相片拍摄角度
        if (degree != 0) {//旋转照片角度，防止头像横着显示
            bm = rotateBitmap(bm, degree);
        }
        String name = filePath.substring(filePath.lastIndexOf("/") + 1);
        File outputFile = new File(targetPath, name);
        FileUtils.createOrExistsFile(outputFile);
        if (!outputFile.exists()) {
            outputFile.getParentFile().mkdirs();
            //outputFile.createNewFile();
        } else {
            outputFile.delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(outputFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bm.compress(Bitmap.CompressFormat.JPEG, quality, out);
        return outputFile.getPath();
    }


    /**
     * 获取照片角度
     *
     * @param path
     * @return
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转照片
     *
     * @param bitmap
     * @param degress
     * @return
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
        if (bitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
            return bitmap;
        }
        return bitmap;
    }

    /**
     * 动态计算出图片的inSampleSize
     *
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    /**
     * @param options
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * 对图片质量压缩
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        //循环判断如果压缩后图片是否大于50kb,大于继续压缩
        while (baos.toByteArray().length / 1024 > 50) {
            //清空baos
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
            options -= 10;//每次都减少10
        }
        //把压缩后的数据baos存放到ByteArrayInputStream中
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        //把ByteArrayInputStream数据生成图片
        Bitmap newBitmap = BitmapFactory.decodeStream(isBm, null, null);
        return newBitmap;
    }

    public static int calculateFileInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


}
