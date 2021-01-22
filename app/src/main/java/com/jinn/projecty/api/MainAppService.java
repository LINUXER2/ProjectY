package com.jinn.projecty.api;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

import com.jinn.frameapi.interfaces.IServiceInterface;
import com.jinn.projecty.utils.LogUtils;

/**
 * Created by jinnlee on 2021/1/21.
 */
public class MainAppService implements IServiceInterface {

    private Context mContext;
    private final String TAG="MainAppService";

    public void setContext(Context context){
        mContext = context;
    }

    public MainAppService(){

    }

    /**
     * 通过主module跳转到其它module
     * @param intent
     * @param context
     */
    public void startActivity(Intent intent,Context context){
        try {
            context.startActivity(intent);
        }catch (ActivityNotFoundException e){
            LogUtils.e(TAG,"startActivity,error:"+e.toString());
        }
    }

}
