package com.ice.httpclient.utils;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.hardware.fingerprint.FingerprintManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.ice.httpclient.MyApplication;
import com.ice.httpclient.R;
import com.ice.httpclient.config.ConfigKeys;
import com.ice.net.http.ApiService;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Desc:
 * Created by icewater on 2024/01/02.
 */
public class Utils {
    private static final String TAG = "Utils";

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static void saveProperty(SharedPreferences sharedPreferences, String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static void saveProperty(SharedPreferences sharedPreferences, String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void saveProperty(SharedPreferences sharedPreferences, String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearProperties(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * Android 11模拟器中getExternalCacheDir()结果为空，不知真机如何，先这样兼容
     *
     * @return
     */
    public static File getExternalCacheDir() {
        assert MyApplication.getContext() != null;
        File file = MyApplication.getContext().getExternalCacheDir();
        if (file == null) {
            file = MyApplication.getContext().getExternalFilesDir(null);
        }
        return file;
    }

    public static Drawable getDrawable(String resName, Context context) {
        int resId = context.getResources()
                .getIdentifier(resName, "drawable", context.getPackageName());
        if (resId > 0) {
            return context.getResources().getDrawable(resId);
        } else {
            return null;
        }
    }

    public static Drawable getDrawable(int drawableResId) {
        return MyApplication.getContext().getResources().getDrawable(drawableResId);
    }

    public static int getDrawableResId(String resName, Context context) {
        int resId = context.getResources()
                .getIdentifier(resName, "drawable", context.getPackageName());
        return resId;
    }

    public static String getString(int resId) {
        return MyApplication.getContext().getString(resId);
    }

    public static String getString(String resName, Context context) {
        int resId = context.getResources()
                .getIdentifier(resName, "string", context.getPackageName());
        if (resId > 0) {
            return MyApplication.getContext().getString(resId);
        } else {
            return null;
        }
    }

    public static int getStringResId(String resName, Context context) {
        int resId = context.getResources()
                .getIdentifier(resName, "string", context.getPackageName());
        return resId;
    }

    public static int getColor(int resId) {
        return MyApplication.getContext().getResources().getColor(resId);
    }

    public static int getColorAttr(Context context, int resId, boolean fromResource) {
        if (fromResource) {
            return getColor(resId);
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resId, typedValue, true);
        return typedValue.data;
    }

    public static int getResIdAttr(Context context, int resId, boolean fromResource) {
        if (fromResource) {
            return getColor(resId);
        }
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(resId, typedValue, true);
        return typedValue.resourceId;
    }

    public static int getDimen(int resId) {
        return MyApplication.getContext().getResources().getDimensionPixelSize(resId);
    }

    public static float getDimenF(int resId) {
        return MyApplication.getContext().getResources().getDimension(resId);
    }

    /**
     * 是否请求的所有全权限都被授权
     *
     * @return
     */
    public static boolean isAllPermissionGranted(int[] grantResults) {
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param filePath
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (isFileExist(filePath)) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];

                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);

            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection, selectionArgs);

            }

        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);

        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            uri.getPath();

        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * 判断一个文件是否存在
     */
    public static boolean isFileExist(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    public static String base64encode(String content) {
        try {
            return Base64.encodeToString(content.getBytes("UTF-8"), Base64.NO_WRAP);
        } catch (Exception e) {
            return content;
        }
    }

    /**
     * 当version0大于version1时返回true
     * 当version0小于等于version1时返回false
     *
     * @param version0
     * @param version1
     * @return
     */
    public static boolean versionBigThan(String version0, String version1) {
        try {
            String[] version0s = version0.split("\\.");
            String[] version1s = version1.split("\\.");
            int minLength = Math.min(version0s.length, version1s.length);
            for (int i = 0; i < minLength; i++) {
                if (Integer.valueOf(version0s[i]) > Integer.valueOf(version1s[i])) {
                    return true;
                } else if (Integer.valueOf(version0s[i]) < Integer.valueOf(version1s[i])) {
                    return false;
                }
            }
            return version0s.length > version1s.length;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 关键词高亮，考虑大小写
     *
     * @param targetString
     * @param keyword
     * @param color        形如 "#FFEEAA"
     * @return
     */
    public static String highlightedText(String targetString, String keyword, String color) {
        if (targetString == null) {
            return "";
        }
        if (targetString.contains("~\\^") || targetString.contains("\\^~") ||
                keyword.equals("~\\^") || keyword.equals("\\^~") ||
                keyword.equals("~") || keyword.equals("\\^")) {
            return targetString;
        }
        try {
            Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            Matcher m = p.matcher(targetString);
            Set<String> sets = new HashSet<String>();
            while (m.find()) {
                sets.add(m.group());
            }
            Iterator<String> iterator = sets.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                targetString = targetString.replaceAll(key, "~\\^" + key + "\\^~");
            }
            targetString = targetString.replaceAll("~\\^", "<font color='" + color + "'>");
            targetString = targetString.replaceAll("\\^~", "</font>");
            return targetString;
        } catch (Exception e) {
            return targetString;
        }

    }

    /**
     * @param targetString
     * @param keyword
     * @param color
     * @return
     */
    public static Spannable highlightedSpanned(String targetString, String keyword, @ColorInt int color) {
        if (targetString == null) {
            return new SpannableString("");
        }
        SpannableString s = new SpannableString(targetString);
        if (keyword.equals(".")) {
            return s;
        }
        try {
            Pattern p = Pattern.compile(keyword, Pattern.CASE_INSENSITIVE);
            Matcher matcher = p.matcher(s);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                s.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception ignore) {
        }
        return s;
    }

    /**
     * 获取指纹识别功能是否有效
     */
    public static boolean isFingerprintEnable() {
        Context context = MyApplication.getContext();
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                FingerprintManager fingerprintManager = (FingerprintManager)
                        context.getSystemService(Context.FINGERPRINT_SERVICE);
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                boolean isHardwareDetected = fingerprintManager.isHardwareDetected();
                boolean hasEnrolledFingerprints = fingerprintManager.hasEnrolledFingerprints();
                return isHardwareDetected && hasEnrolledFingerprints;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean isInteger(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static boolean isEmail(String str) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        String regex = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }

    //判断是否为日期格式
    public static boolean matchDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = format.parse(date);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取日期描述
     * 比如 昨天 12:00
     * 12：00
     * <p>
     * 星期一 12:00
     * 2017年05月02日 12:00
     *
     * @param targetDate 目标日期字符串 2018-12-12 12:32:45
     * @param format     目标格式 yyyy-MM-dd HH:mm:ss
     * @return 转换后的描述字符串
     */
    public static String getTimeRecentDescribe(String targetDate, String format) {
        if (targetDate == null) {
            return "";
        }
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        try {
            target.setTime(new SimpleDateFormat(format).parse(targetDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return getTimeDescribe(now, target, false);
    }

    public static String getTimeRecentDescribe(long targetTime) {
        if (targetTime == 0) {
            return "";
        }
        Calendar now = Calendar.getInstance();
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(targetTime);
        return getTimeDescribe(now, target, false);
    }


    private static String getTimeDescribe(Calendar now, Calendar target, boolean withTime) {
        if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {
            if (now.get(Calendar.WEEK_OF_YEAR) == target.get(Calendar.WEEK_OF_YEAR)) {
                int nowDay = now.get(Calendar.DAY_OF_WEEK);
                int targetDay = target.get(Calendar.DAY_OF_WEEK);
                String time = new SimpleDateFormat("HH:mm",
                        Locale.getDefault()).format(target.getTime());
                if (nowDay == targetDay) {
                    return time;
                } else if (nowDay - targetDay == 1) {
                    return Utils.getString(R.string.base_yesterday) + time;
                } else if (nowDay - targetDay > 1) {
                    return target.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG,
                            Locale.getDefault()) + " " + time;
                }
            }
        }
        return new SimpleDateFormat(withTime ? "yy/MM/dd HH:mm" : "yy/MM/dd",
                Locale.getDefault()).format(target.getTime());
    }

    /**
     * 将long类型转化为时间字符串
     */
    public static String long2TimeString(long timeStamp, String format) {
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(timeStamp);
        return new SimpleDateFormat(format, Locale.getDefault()).format(target.getTime());
    }

    //获取是哪一年
    public static int getYear(long timeStamp) {
        Calendar target = Calendar.getInstance();
        target.setTimeInMillis(timeStamp);
        return target.get(Calendar.YEAR);
    }

    /**
     * 将时间字符串转化为long类型
     *
     * @param targetDate 目标日期字符串 2018-12-12 12:32:45
     * @param format     目标格式 yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static long TimeString2Long(String targetDate, String format) {
        Calendar target = Calendar.getInstance();
        try {
            if (!StringUtils.isEmpty(targetDate)) {
                target.setTime(new SimpleDateFormat(format).parse(targetDate));
            }
            return target.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return target.getTimeInMillis();
    }

    /**
     * 把px转换成dp
     *
     * @param pxvalue
     * @return
     */
    public static int px2dp(int pxvalue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxvalue / scale + 0.5f);
    }

    //转换文件大小为 KB MB
    public static String convertFileSize(long fileSize) {
        if (fileSize > 1024 * 1024) {
            return String.format("%.2f", fileSize / (1024f * 1024f)) + "MB";
        } else if (fileSize > 1024) {
            return String.format("%.2f", fileSize / 1024f) + "KB";
        } else {
            return String.format("%.2f", fileSize / 1f) + "B";
        }
    }

    /**
     * dp转换成px
     *
     * @param dpvalue
     * @return
     */
    public static int dp2px(int dpvalue) {
        float scale = MyApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpvalue * scale + 0.5f);
    }

    /**
     * sp 转换成px
     *
     * @param spvalue
     * @return
     */
    public static int sp2px(int spvalue) {
        float scale = MyApplication.getContext().getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (scale * spvalue + 0.5f);
    }

    /**
     * px 转换成sp
     *
     * @param value
     * @return
     */
    public static int px2sp(int value) {
        float fontScale = MyApplication.getContext().getResources()
                .getDisplayMetrics().scaledDensity;
        return (int) (value / fontScale + 0.5f);
    }

    /**
     * 删除某个文件夹下的所有文件
     * 递归删除某个文件夹下面的所有的文件
     *
     * @param dir
     * @return
     */
    public static boolean deleteFile(File dir) {

        if (dir.isDirectory()) {
            String[] child = dir.list();
            for (int i = 0; i < child.length; i++) {
                boolean result = deleteFile(new File(dir, child[i]));
                if (!result) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    //判断wifi是否连接
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable() &&
                        mWiFiNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    //判断数据流量是否可用
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable() &&
                        mMobileNetworkInfo.getState() == NetworkInfo.State.CONNECTED;
            }
        }
        return false;
    }

    //获取当前语言
    public static String getLanguage() {
        Locale locale;
        if (MyApplication.getContext() != null) {
            locale = MyApplication.getContext().getResources().getConfiguration().locale;
        } else {
            locale = Locale.getDefault();
        }
        String language = locale.getLanguage();
        if (language.contains("en")) {
            return "en";
        } else if (language.contains("zh")) {
            return "zh_CN";
        } else {
            return language;
        }
    }

    public static String getLanguageForHeader() {
        String language = getLanguage();
        if (language.contains("en")) {
            return "en_US";
        } else {
            return "zh_CN";
        }
    }

    public static String getSetLanguage(Context context, String dft) {
        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        return sp.getString(ConfigKeys.SP_LANGUAGE, dft);
    }

    public static String md5(Object object) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(toByteArray(object));
        } catch (NoSuchAlgorithmException var7) {
            throw new RuntimeException("Huh, MD5 should be supported?", var7);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        byte[] var3 = hash;
        int var4 = hash.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            byte b = var3[var5];
            if ((b & 255) < 16) {
                hex.append("0");
            }

            hex.append(Integer.toHexString(b & 255));
        }

        return hex.toString();
    }

    private static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException var4) {
            var4.printStackTrace();
        }
        return bytes;
    }

    //判断应用处于前台还是后台
    public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isForeground(Context context, String className) {
        if (context == null || StringUtils.isEmpty(className)){
            return false;
        }
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }

    public static int getFileIconByFileName(String fileName) {
        String lowerName = fileName.toLowerCase();
        if (lowerName.endsWith("zip")) {
            return R.drawable.base_zip;
        } else if (lowerName.endsWith("rar")) {
            return R.drawable.base_rar;
        } else if (lowerName.endsWith("doc") || lowerName.endsWith("docx")) {
            return R.drawable.base_doc;
        } else if (lowerName.endsWith("xls") || lowerName.endsWith("xlsx")) {
            return R.drawable.base_xls;
        } else if (lowerName.endsWith("txt")) {
            return R.drawable.base_txt;
        } else if (lowerName.endsWith("ppt") || lowerName.endsWith("pptx")) {
            return R.drawable.base_ppt;
        } else if (lowerName.endsWith("pdf")) {
            return R.drawable.base_pdf;
        } else if (lowerName.endsWith("gif")) {
            return R.drawable.base_gif;
        } else if (lowerName.endsWith("png") || lowerName.endsWith("jpg") ||
                lowerName.endsWith("jpeg") || lowerName.endsWith("bmp")) {
            return R.drawable.base_image;
        } else if (lowerName.endsWith("mp3")) {
            return R.drawable.base_mp3;
        } else {
            return R.drawable.base_unknown;
        }
    }

    public static boolean isAppExist(Context context, String packageName) {
        List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
        for (PackageInfo packageInfo : packageInfos) {
            if (packageName.equalsIgnoreCase(packageInfo.packageName)) {
                return true;
            }
        }
        return false;
    }

    public static RequestBody getRequestBody(String content) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), content);
        return requestBody;
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     *
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isPad(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 返回当前屏幕是否为竖屏。
     *
     * @param context
     * @return 当且仅当当前屏幕为竖屏时返回true, 否则返回false。
     */
    public static boolean isScreenOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    public static boolean bitmapToFile(Context context, Bitmap bitmap, String filePath) {
        File file = new File(filePath);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
            ImageUtils.addImage(context.getContentResolver(), file.getAbsolutePath());
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    //检查当前系统是否已开启暗黑模式
    public static boolean isDarkModeStatus(Context context) {
        int mode = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }

}
