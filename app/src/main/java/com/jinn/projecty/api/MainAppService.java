package com.jinn.projecty.api;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.jinn.projecty.frameapi.service.IMainAppService;
import com.jinn.projecty.utils.LogUtils;

/**
 * Created by jinnlee on 2021/1/21.
 */
public class MainAppService implements IMainAppService {

    private Context mContext;
    private final String TAG="MainAppService";

    public void setContext(Context context){
        mContext = context;
    }

    public MainAppService(){
        LogUtils.d(TAG,"MainAppService,constructor1");
    }
    public MainAppService(Context context){
        mContext = context;
        LogUtils.d(TAG,"MainAppService,constructor2");
    }

    /**
     * 通过主module跳转到其它module
     * @param intent
     * @param context
     */
    @Override
    public void startActivity(Intent intent,Context context){
        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            LogUtils.e(TAG,"startActivity,error:"+e.toString());
        }
    }

    @Override
    public void init() {

    }
}
