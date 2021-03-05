package com.jinn.projecty.main.model;

import android.app.Application;

import com.jinn.projecty.base.BaseModel;
import com.jinn.projecty.base.BaseViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

public class VideoDetailViewModel extends BaseViewModel<VideoDetailModel> {
    public VideoDetailViewModel(@NonNull Application application) {
        super(application);
    }

    public VideoDetailViewModel(@Nullable Application application, VideoDetailModel model) {
        super(application, model);
        mModel = model;
    }

}
