package com.jinn.projecty.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Process;

/**
 * Created by jinnlee on 2021/1/20.
 *   工作线程，串行处理任务
 */
public class HeavyWorkThread {
    private final static String TAG = "HeavyWorkerThread";
    private static HandlerThread sHThread = null;

    private static Handler sHandler = null;

    private static int sId = 0;

    public static synchronized Handler getHandler() {

        if (sHThread == null) {
            String name = TAG + sId;
            sHThread = new HandlerThread(name);
            sHThread.setPriority(Process.THREAD_PRIORITY_BACKGROUND);
            sHThread.start();
            ++sId;
        }
        if (sHandler == null) {
            sHandler = new Handler(sHThread.getLooper());
        }
        return sHandler;
    }

    public static Looper getLooper() {
        if (sHThread == null) {
            return getHandler().getLooper();
        }
        return sHThread.getLooper();
    }

    public static void reset() {
        if (sHandler == null || sHThread == null) {
            return;
        }

        sHThread.quit();
        sHandler.removeCallbacksAndMessages(null);
        sHThread = null;
        sHandler = null;
    }
}
