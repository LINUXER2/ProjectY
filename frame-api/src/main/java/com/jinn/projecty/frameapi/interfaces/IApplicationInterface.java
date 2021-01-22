package com.jinn.projecty.frameapi.interfaces;

import android.content.res.Configuration;

/**
 * Created by jinnlee on 2021/1/19.
 *  各个子module application生命周期
 */
public interface IApplicationInterface {
    void onCreate();
    void onLowMemory();
    void onTrimMemory(int level);
    void onConfigurationChanged(Configuration newConfig);
    void onTerminate();
}
