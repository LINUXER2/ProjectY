package com.jinn.projecty;

import android.content.res.Configuration;

import com.jinn.projecty.frameapi.base.BaseApplication;

/**
 * Created by jinnlee on 2021/1/19.
 */
public class ProjectYApplication extends BaseApplication {
    public ProjectYApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
