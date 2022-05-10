package com.jinn.projecty.utils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

public class ProcessUtils {
    private static final String TAG = "ProcessUtils";
    private static  final String PROCESS_MAIN = "com.jinn.projecty";
    private static  final String PROCESS_REMOTE = "com.jinn.projecty:remote";

    /**
     * 获取当前进程名
     *
     * @param cxt
     * @return
     */
    public static String getProcessName(Context cxt) {
        String processName = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            processName= ((Application)cxt.getApplicationContext()).getProcessName();
            LogUtils.d(TAG, "processName1 :" + processName);
            return processName;
        }
        try {
            final Method declaredMethod = Class.forName("android.app.ActivityThread",
                    false, cxt.getClassLoader()).getDeclaredMethod("currentProcessName", (Class<?>[]) new Class[0]);
            declaredMethod.setAccessible(true);
            final Object invoke = declaredMethod.invoke(null, new Object[0]);
            if (invoke instanceof String) {
                processName = (String) invoke;
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "getProcesserror: " + e);
        }
        LogUtils.d(TAG, "processName2 :" + processName);
        return processName;
    }

    public static boolean isInMainProcess(Context context){
        return TextUtils.equals(PROCESS_MAIN,getProcessName(context));
    }
}
