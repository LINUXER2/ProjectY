package com.jinn.projecty.frameapi.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import com.jinn.projecty.frameapi.interfaces.IApplicationInterface;
import com.jinn.projecty.frameapi.service.ServiceManager;
import com.jinn.projecty.utils.LogUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

/**
 * Created by jinnlee on 2021/1/19.
 *  Application 基类，负责将生命周期同步给各个子module；
 */
public class BaseApplication extends Application implements IApplicationInterface {
    private static final String TAG = "BaseApplication";
    private ArrayList<IApplicationInterface> mModuleApplications =new ArrayList<>();


    public BaseApplication() {
        super();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        LogUtils.d(TAG,"BaseApplication,attachBaseContext");
        createApplications(base);
        createServices(base);
    }


    /**
     *  反射load各个子module中的application
     * @param context
     */
    private void createApplications(Context context){
        ArrayList<String> classNames = new ArrayList<>();
        classNames.add("com.jinn.projecty.main.MainApplication");
        classNames.add("com.jinn.projecty.network.NetWorkApplication");

        for (String className : classNames) {
            Class<?>clazz = null;
            try {
                clazz =  Class.forName(className);
            }catch (ClassNotFoundException e){
                clazz =null;
                Log.d("jinn","ClassNotFoundException:"+e.toString());
            }
            if(clazz==null){
                continue;
            }

            //反射获取构造函数,带参
            try {
                Constructor constructor =  clazz.getConstructor(Context.class);
                constructor.setAccessible(true);
                IApplicationInterface application = (IApplicationInterface) constructor.newInstance(context);
                mModuleApplications.add(application);
                continue;
            }catch (Exception e){
                Log.d("jinn","get constructer context error,"+e.toString());
            }

            //若没有带参的构造函数，则获取无参构造函数
            try{
                IApplicationInterface application = (IApplicationInterface)clazz.newInstance();
                mModuleApplications.add(application);
            }catch (Exception e){
                Log.d("jinn","get constructer error,"+e.toString());
            }

        }
    }


    /**
     * 反射调用各子module中对外暴露的接口
     * @param context
     */
    private void createServices(Context context){
        ServiceManager.getInstance().loadServices(context);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d(TAG,"BaseApplication,onCreate");
        for(IApplicationInterface application : mModuleApplications){
            application.onCreate();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        for(IApplicationInterface application : mModuleApplications){
            application.onTerminate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        for(IApplicationInterface application : mModuleApplications){
            application.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        for(IApplicationInterface application : mModuleApplications){
            application.onLowMemory();
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        for(IApplicationInterface application : mModuleApplications){
            application.onTrimMemory(level);
        }
    }

}
