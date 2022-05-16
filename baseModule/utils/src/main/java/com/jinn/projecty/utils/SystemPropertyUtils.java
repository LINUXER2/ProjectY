package com.jinn.projecty.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.HashMap;

public class SystemPropertyUtils {
    private static int mScreenWidth = -1;
    private static int mScreenHeight = -1;
    private static final String TAG= "SystemPropertyUtils";
    private static HashMap<String, String> mSysPropMap = new HashMap<>();

    public static int getScreenWidth(Context context) {
        if(mScreenWidth != -1) {
            return mScreenWidth;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealMetrics(dm);
        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;
        if (screenHeight > screenWidth) {
            mScreenWidth = screenWidth;
        } else {
            mScreenWidth = screenHeight;
        }
        return mScreenWidth;
    }

    public static int getScreenHeight(Context context) {
        if(mScreenHeight != -1) {
            return mScreenHeight;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getRealMetrics(dm);
        int screenHeight = dm.heightPixels;
        int screenWidth = dm.widthPixels;
        if (screenHeight > screenWidth) {
            mScreenHeight = screenHeight;
        } else {
            mScreenHeight = screenWidth;
        }
        return mScreenHeight;
    }


    public static String getSystemProperties(String key, String def) {
        if (mSysPropMap != null && mSysPropMap.containsKey(key)) {
            return mSysPropMap.get(key);
        }
        String result;
        try {
            Class<?> ownerClass = Class.forName("android.os.SystemProperties");
            Class<?> classes[] = new Class[2];
            classes[0] = String.class;
            classes[1] = String.class;
            Method m = ownerClass.getDeclaredMethod("get", classes);
            result = (String) m.invoke(null, key, def);
            if (mSysPropMap != null) {
                mSysPropMap.put(key, result);
            }
        } catch (Exception e) {
            result = def;
            LogUtils.e(TAG, "getSystemProperties exception, e = " + e);
        }
        return result;
    }

}
