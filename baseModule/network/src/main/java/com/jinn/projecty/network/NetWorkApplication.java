package com.jinn.projecty.network;

import android.content.Context;
import android.content.res.Configuration;

import com.facebook.stetho.Stetho;
import com.jinn.projecty.frameapi.interfaces.IApplicationInterface;

/**
 * Created by jinnlee on 2021/3/3.
 */
public class NetWorkApplication implements IApplicationInterface {

    private Context mContext;

    public NetWorkApplication(){

    }

    public NetWorkApplication(Context context){
        mContext = context;
    }

    @Override
    public void onCreate() {
        openDebugMode();
    }

    private void openDebugMode(){
        Stetho.initializeWithDefaults(mContext);
    }

    @Override
    public void onLowMemory() {

    }

    @Override
    public void onTrimMemory(int level) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

    }

    @Override
    public void onTerminate() {

    }
}
