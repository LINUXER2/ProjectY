package com.jinn.projecty.network;

import android.content.Context;
import android.content.res.Configuration;

import com.facebook.stetho.Stetho;
import com.jinn.projecty.frameapi.interfaces.IApplicationInterface;

import static com.facebook.stetho.Stetho.newInitializerBuilder;

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
        if (BuildConfig.DEBUG) {
            Stetho.initialize(
                    newInitializerBuilder(mContext)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(mContext))
                            .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(mContext))
                            .build());
        }
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
