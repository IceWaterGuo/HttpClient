package com.ice.httpclient.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.util.Log;
import android.view.WindowManager;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Random;

/**
 * Desc:
 * Created by icewater on 2021/12/06.
 */
public class UIUtils {

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density + 0.5f);
        return px;
    }

    /**
     * px转dp
     *
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        int dp = (int) (px / density + 0.5f);
        return dp;
    }

    /**
     * sp转px
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
    /**
     * px转sp
     *
     * 将px转换为sp
     */
    public static int px2sp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }


    /**
     * 产生随机颜色
     * @return
     */
    public static int randomColor(){
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        return ranColor;
    }


    public static int getWindowWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 缩放bitmap
     * @param activity
     * @param imagePath
     * @return
     */
    public static Bitmap zoomBitmap(Activity activity, String imagePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        WindowManager windowManager = activity.getWindowManager();
        int winWidth = windowManager.getDefaultDisplay().getWidth();
        int winHeight = windowManager.getDefaultDisplay().getHeight() - getStatusBarHeight(activity);

        int scale = 1;
        int scaleWidth = outWidth / winWidth;
        int scaleHeight = outHeight / winHeight;
        if (scaleWidth >= scaleHeight && scaleWidth > 1) {
            scale = scaleWidth;
        }
        if (scaleHeight > scaleWidth && scaleHeight > 1) {
            scale = scaleHeight;
        }
        options.inJustDecodeBounds = false;
        options.inSampleSize = scale;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
        int degree = readPictureDegree(imagePath);
        bitmap = rotaingImageView(degree, bitmap);
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
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
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Log.e("marykay", "angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
    /**
     * 通过反射的方式获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取字体
     * @param context
     * @param fontPath
     * @return
     */
    public static Typeface getTypeface(Context context, String fontPath){
        AssetManager assetManager =context.getApplicationContext().getAssets();
        Typeface tf=Typeface.createFromAsset(assetManager, fontPath);
        return tf;
    }
}
