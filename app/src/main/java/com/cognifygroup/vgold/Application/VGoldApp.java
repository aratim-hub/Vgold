package com.cognifygroup.vgold.Application;

import android.content.Context;
import android.content.SharedPreferences;

import com.cognifygroup.vgold.R;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import androidx.multidex.MultiDexApplication;

/**
 * Created by shivraj on 6/15/18.
 */

public class VGoldApp extends MultiDexApplication {

    private static ImageLoader mImageLoader;
    private static Context mContext;
    private static SharedPreferences mSharedPreferences;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // The following line triggers the initialization of ACRA
        // ACRA.init(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mSharedPreferences = mContext.getSharedPreferences("VGold", MODE_PRIVATE);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)

                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .memoryCache(new WeakMemoryCache())
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(config);
    }

    public static Context getContext() {
        return mContext;
    }

    public static ImageLoader getmImageLoader() {
        return mImageLoader;
    }

    public static void onSetUserDetails(String uid, String first, String last,
                                        String email, String no,
                                        String qr,String pan_no,
                                        String address,String city,
                                        String state,String userimg) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString("USERID", uid);
        editor.putString("FIRST", first);
        editor.putString("LAST", last);
        editor.putString("EMAIL", email);
        editor.putString("NO", no);
        editor.putString("QR", qr);
        editor.putString("PAN_NO", pan_no);
        editor.putString("ADDRESS", address);
        editor.putString("CITY", city);
        editor.putString("STATE", state);
        editor.putString("USERIMG", userimg);

        editor.commit();
    }

    public static void onSetUserRole(String user_role, String validity){

        SharedPreferences.Editor editor1 = mSharedPreferences.edit();

        editor1.putString("USERROLE", user_role);
        editor1.putString("VAILDITY", validity);

        editor1.commit();

    }

    public static void onSetVersionCode(String version_code){

        SharedPreferences.Editor editor2 = mSharedPreferences.edit();

        editor2.putString("version_code", version_code);

        editor2.commit();

    }

    public static String onGetVersionCode() {
        return mSharedPreferences.getString("version_code", "");
    }


    public static String onGetValidity() {
        return mSharedPreferences.getString("VAILDITY", "");
    }


    public static String onGetUserRole() {
        return mSharedPreferences.getString("USERROLE", "");
    }

    public static String onGetFirst() {
        return mSharedPreferences.getString("FIRST", "");
    }

    public static String onGetLast() {
        return mSharedPreferences.getString("LAST", "");
    }

    public static String onGetNo() {
        return mSharedPreferences.getString("NO", "");
    }

    public static String onGetUerId() {
        return mSharedPreferences.getString("USERID", "");
    }

    public static String onGetEmail() {
        return mSharedPreferences.getString("EMAIL", "");
    }

    public static String onGetQrCode() {
        return mSharedPreferences.getString("QR", "");
    }

    public static String onGetPanNo() {
        return mSharedPreferences.getString("PAN_NO", "");
    }

    public static String onGetAddress() {
        return mSharedPreferences.getString("ADDRESS", "");
    }

    public static String onGetCity() {
        return mSharedPreferences.getString("CITY", "");
    }

    public static String onGetState() {
        return mSharedPreferences.getString("STATE", "");
    }

    public static String onGetUserImg() {
        return mSharedPreferences.getString("USERIMG", "");
    }

}
