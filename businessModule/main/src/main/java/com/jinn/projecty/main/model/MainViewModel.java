package com.jinn.projecty.main.model;

import android.app.Application;

import com.jinn.projecty.base.BaseViewModel;
import com.jinn.projecty.main.model.MainModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainViewModel extends BaseViewModel <MainModel>{
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MainViewModel(@Nullable Application application, MainModel model) {
        super(application, model);
        mModel = model;
    }

}
