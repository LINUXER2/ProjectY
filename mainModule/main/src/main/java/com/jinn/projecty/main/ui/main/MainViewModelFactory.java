package com.jinn.projecty.main.ui.main;

import android.app.Application;

import com.jinn.projecty.base.BaseModel;
import com.jinn.projecty.base.BaseViewModelFactory;

/**
 * Created by jinnlee on 2021/1/22.
 */
public class MainViewModelFactory extends BaseViewModelFactory {
    public MainViewModelFactory(Application application) {
        super(application);
    }

    @Override
    public BaseModel onBindModel(Class modelClass) {
        return new MainModel();
    }
}
