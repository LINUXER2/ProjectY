package com.jinn.projecty.main.ui.main;

import android.app.Application;

import com.jinn.projecty.base.BaseModel;
import com.jinn.projecty.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MainViewModel extends BaseViewModel <MainModel>{
    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    public MainViewModel(@Nullable Application application, MainModel model) {
        super(application, model);
    }

}
