package com.jinn.projecty.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by jinnlee on 2021/1/22.
 */
public abstract class BaseViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private static ViewModelProvider sInstance;
    private Application mApplication;

    public BaseViewModelFactory(Application application) {
        this.mApplication = application;
    }

    public abstract BaseModel onBindModel(Class modelClass);

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BaseViewModel<BaseModel>(mApplication, onBindModel(modelClass));
    }
}
