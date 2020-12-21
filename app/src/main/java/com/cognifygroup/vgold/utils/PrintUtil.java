package com.cognifygroup.vgold.utils;

import android.content.Context;

import com.cognifygroup.vgold.R;


public class PrintUtil {

    public static void showToast(Context context, String msg) {
        BaseActivity.showToast(context, msg);
    }

   public static String showNetworkAvailableToast(Context context) {
        try {
            if (BaseActivity.isNetworkAvailable(context)) {
                BaseActivity.showToast(context, "");
                return "";
            } else {
                BaseActivity.showToast(context, "");
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}