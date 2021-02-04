package com.jinn.projecty.network;

import android.util.Log;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by jinnlee on 2021/1/26.
 */
public class RetrofitManager {
    private static RetrofitManager sInstance;
    private HashMap<Class, Object> mApis = new HashMap<>();

    private RetrofitManager(){
    }

    public static RetrofitManager getInstance(){
        if(sInstance==null){
            synchronized (RetrofitManager.class){
                if(sInstance==null){
                    sInstance = new RetrofitManager();
                }
            }
        }
        return sInstance;
    }


    /**
     * 创建一个retrofit service并缓存该service
     * @param service
     * @param baseUrl
     * @param <T>
     * @return
     */
    public <T> T createService(Class<T> service, String baseUrl) {
        if (!mApis.containsKey(service)) {
            Retrofit retrofit = getRetrofit(baseUrl);
            T clazz = retrofit.create(service);
            mApis.put(service, clazz);
        }
        return (T)mApis.get(service);
    }

    public void clearAllServices(){
        mApis.clear();
    }

    private Retrofit getRetrofit(String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()); //返回observalle，若不加，则默认返回Call
        return builder.build();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
              Log.d("OkHttp",message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

}
