package com.jinn.projecty.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

/**
 * Created by jinnlee on 2021/1/20.
 */
public class BaseViewModel<BM extends BaseModel> extends AndroidViewModel {

    protected BM mModel;

    //公共能力，子modle可以直接监听这两个值，用于处理页面加载中和显示异常状态等
    private MutableLiveData<Boolean> mLiveDataShowLoading;
    private MutableLiveData<Boolean> mLiveDataShowError;

    public BaseViewModel(@NonNull Application application) {
        super(application);
    }

    public BaseViewModel(@Nullable Application application,BM model){
        super(application);
        mModel = model;
    }


    /**
     * liveData:是否显示加载中
     * @return
     */
    public MutableLiveData getLiveDataShowLoading(){
        if(mLiveDataShowLoading==null){
            mLiveDataShowLoading = new MutableLiveData<Boolean>();
        }
        return mLiveDataShowLoading;
    }

    /**
     * liveData:是否显示错误页面
     * @return
     */
    public MutableLiveData getLiveDataShowError(){
       if(mLiveDataShowError==null){
           mLiveDataShowError = new MutableLiveData<Boolean>();
       }
       return mLiveDataShowError;
    }

    @NonNull
    @Override
    public <T extends Application> T getApplication() {
        return super.getApplication();
    }
}
