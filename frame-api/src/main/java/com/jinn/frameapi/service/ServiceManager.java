package com.jinn.frameapi.service;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.jinn.frameapi.interfaces.IServiceInterface;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinnlee on 2021/1/19.
 */
public class ServiceManager {
    private static ServiceManager sInstance;
    private HashMap<String, IServiceInterface> mModuleServices = new HashMap<>();

    private ServiceManager(){

    }

    public static ServiceManager getInstance(){
        if(sInstance==null){
            synchronized (Service.class){
                if(sInstance==null){
                    sInstance = new ServiceManager();
                }
            }
        }
        return sInstance;
    }

    /**
     * 反射load各个module，获取构造函数
     * @param context
     */
    public void loadServices(Context context){
         HashMap<String,String>hashMap =new HashMap<>();
         hashMap.put("basemodule","com.jinn.projectx.basemodule");

         for(Map.Entry entry : hashMap.entrySet()){
             String key = (String)entry.getKey();
             String className = (String)entry.getValue();
             Class clazz = null;
             try {
                 clazz = Class.forName(className);
             }catch (ClassNotFoundException e){
                 clazz = null;
                 Log.d("jinn","class not found,"+e.toString());
             }
             if(clazz == null){
                 return;
             }

             try {
                 Constructor constructor = clazz.getConstructor(Context.class);
                 constructor.setAccessible(true);
                 IServiceInterface iServiceInterface = (IServiceInterface)constructor.newInstance(context);
                 mModuleServices.put(key,iServiceInterface);
                 continue;
             } catch (Exception e) {
                 Log.d("jinn","class not found 2,"+e.toString());
             }

             try {
                 IServiceInterface iServiceInterface =(IServiceInterface) clazz.newInstance();
                 mModuleServices.put(key,iServiceInterface);
             }catch (Exception e){
                 Log.d("jinn","class not found 3,"+e.toString());
             }

         }
    }



    public IServiceInterface getService(String key){
        return mModuleServices.get(key);
    }

}
