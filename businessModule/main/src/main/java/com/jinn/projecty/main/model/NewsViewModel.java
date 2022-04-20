package com.jinn.projecty.main.model;

import android.app.Application;

import com.jinn.projecty.base.BaseViewModel;
import com.jinn.projecty.main.bean.RecommandDataBean;
import com.jinn.projecty.utils.LogUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by jinnlee on 2021/1/25.
 * 业务逻辑处理，负责更新数据，持有model，并监听model数据变化，回调给view
 * 实现LifecycleObserver监听MainFragment生命周期
 *
 */
public class NewsViewModel extends BaseViewModel <NewsModel> implements LifecycleObserver {
    private final String TAG= "MainViewModel";
    private MutableLiveData<RecommandDataBean> mRecommandData;
    public NewsViewModel(@NonNull Application application) {
        super(application);
    }

    public NewsViewModel(@Nullable Application application, NewsModel model) {
        super(application, model);
        mModel = model;
    }

    public MutableLiveData<RecommandDataBean> getVideoLists(){
        if(mRecommandData ==null){
            mRecommandData = new MutableLiveData<>();
        }
        return mRecommandData;
    }

    public void requestMainData(){
        mModel.requestMainData().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecommandDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d(TAG,"onSubscribe,");
                    }

                    @Override
                    public void onNext(RecommandDataBean recommandData) {
                        LogUtils.d(TAG,"onNext:"+recommandData.getNextPageUrl());
                        recommandData.setRecommand(false);
                        mRecommandData.postValue(recommandData);
                        getLiveDataShowLoading().postValue(false);
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

    public void requestMoreData(String url){
        mModel.requestRelateData(url).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<RecommandDataBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(RecommandDataBean bean) {
                        LogUtils.d(TAG,"onNext:"+bean.getNextPageUrl());
                        bean.setRecommand(true);
                        mRecommandData.postValue(bean);
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

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
        LogUtils.d(TAG, "onCreate");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        LogUtils.d(TAG, "onResume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        LogUtils.d(TAG, "onPause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy");
    }
}
