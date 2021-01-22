package com.jinn.projecty.main.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by jinnlee on 2021/1/22.
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static ViewModelFactory sInstance;
    private Application mApplication;

    private ViewModelFactory(){};

    private ViewModelFactory(Application application) {
        this.mApplication = application;
    }

    public static ViewModelFactory getInstance(Application application){
        if(sInstance==null){
            synchronized (ViewModelFactory.class){
                if(sInstance==null){
                    sInstance = new ViewModelFactory(application);
                }
            }
        }
        return sInstance;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T)new MainViewModel(mApplication,new MainModel());
        }else {
            throw new IllegalArgumentException("unknow viewModel:"+modelClass.getName());
        }
    }
}
