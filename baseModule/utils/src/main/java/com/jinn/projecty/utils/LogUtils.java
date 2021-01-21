package com.jinn.projecty.utils;

import android.util.Log;

/**
 * Created by jinnlee on 2021/1/20.
 */
public class LogUtils {
    private static boolean isDubug = true;
    public static void d(String TAG,String msg){
        if(isDubug){
            Log.d(TAG,msg);
        }

    }

    public static void i(String TAG,String msg){
        if(isDubug) {
            Log.i(TAG, msg);
        }
    }

    public static void e(String TAG,String msg){
        if(isDubug){
            Log.e(TAG,msg);
        }

    }
}
