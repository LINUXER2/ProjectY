package com.jinn.projecty.frameapi.service;

import android.content.Context;
import android.content.Intent;

import com.jinn.projecty.frameapi.interfaces.IServiceInterface;

public interface IMainAppService extends IServiceInterface {
    public void startActivity(Intent intent, Context context);
}
