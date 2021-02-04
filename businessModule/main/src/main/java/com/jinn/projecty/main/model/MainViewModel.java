package com.jinn.projecty.main.model;

import android.app.Application;

import com.jinn.projecty.base.BaseViewModel;
import com.jinn.projecty.main.api.RetrofitApi;
import com.jinn.projecty.main.bean.RecommandData;
import com.jinn.projecty.main.model.MainModel;
import com.jinn.projecty.network.RetrofitManager;
import com.jinn.projecty.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * Created by jinnlee on 2021/1/25.
 * 业务逻辑处理，负责更新数据，持有model，并监听model数据变化，回调给view
 */
public class MainViewModel extends BaseViewModel <MainModel>{
    private final String TAG= "MainViewModel";
    private MutableLiveData<List<String>> mVideoLists;
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MainViewModel(@Nullable Application application, MainModel model) {
        super(application, model);
        mModel = model;
    }

    public MutableLiveData<List<String>> getVideoLists(){
        if(mVideoLists==null){
            mVideoLists = new MutableLiveData<>();
        }
        return mVideoLists;
    }

    public void requestMainData(){
        mModel.requestMainData().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecommandData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d(TAG,"onSubscribe,");
                    }

                    @Override
                    public void onNext(RecommandData recommandData) {
                        String response;

                            response = recommandData.toString();
                            getLiveDataShowLoading().postValue(false);
                            LogUtils.d(TAG,"onNext,"+response);

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(TAG,"onError,"+e.toString());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d(TAG,"onComplete");
                    }
                });
    }

}
