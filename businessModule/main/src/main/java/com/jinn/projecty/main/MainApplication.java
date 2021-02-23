package com.jinn.projecty.main;

import android.content.Context;
import android.content.res.Configuration;

import com.jinn.projecty.frameapi.interfaces.IApplicationInterface;
import com.jinn.projecty.utils.LogUtils;

/**
 * Created by jinnlee on 2021/2/24.
 */
public class MainApplication implements IApplicationInterface {
    private final String TAG = "MainApplication";
    private Context mContext;
    public MainApplication() {
        LogUtils.d(TAG,"MainApplication constructor1");
    }

    public MainApplication(Context context){
        mContext = context;
        LogUtils.d(TAG,"MainApplication constructor2");
    }

    @Override
    public void onCreate() {
        LogUtils.d(TAG,"MainApplication onCreate");
    }

    @Override
    public void onLowMemory() {
        LogUtils.d(TAG,"MainApplication onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        LogUtils.d(TAG,"MainApplication onTrimMemory:"+level);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        LogUtils.d(TAG,"MainApplication onConfigurationChanged");
    }

    @Override
    public void onTerminate() {
        LogUtils.d(TAG,"MainApplication onTerminate");
    }
}
