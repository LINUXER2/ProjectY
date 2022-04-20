package com.jinn.projecty.main.model;

import android.app.Application;

import com.jinn.projecty.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsLandingViewModel extends BaseViewModel<NewsLandingModel> {
    public NewsLandingViewModel(@NonNull Application application) {
        super(application);
    }

    public NewsLandingViewModel(@Nullable Application application, NewsLandingModel model) {
        super(application, model);
        mModel = model;
    }

}
