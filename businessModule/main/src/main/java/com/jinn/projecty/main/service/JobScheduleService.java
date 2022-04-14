package com.jinn.projecty.main.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.jinn.projecty.main.message.MyHandler;
import com.jinn.projecty.utils.LogUtils;

import io.reactivex.internal.functions.ObjectHelper;

/**
 * Created by jinnlee on 2021/7/3.
 */

public class JobScheduleService extends JobService {
    private final String TAG  ="JobScheduleService";
    private Messenger mMessenger;
    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtils.d(TAG,"onStartJob，jobId："+params.getJobId());
        Long duration = params.getExtras().getLong("work_duration_key");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sendmessage(MyHandler.MSG_INIT,params.getJobId());
            }
        },duration);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        LogUtils.d(TAG,"onStopJob,jobId:"+params.getJobId());
        return false;
    }

    private void sendmessage(int messageId, Object params){
        if(mMessenger==null){
            return;
        }
        Message m = Message.obtain();
        m.what = messageId;
        m.obj = params;
//        try {
//            mMessenger.send(m);
//        }catch (RemoteException e){
//            LogUtils.e(TAG,"send msg error:"+e.toString());
//        }

    }

    @Override
    public void onCreate() {
        LogUtils.d(TAG,"onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d(TAG,"onStartCommand");
        mMessenger  = intent.getParcelableExtra("message_init");

        return START_NOT_STICKY;
    }

    @Override
    public void onRebind(Intent intent) {
        LogUtils.d(TAG,"onRebind");
        super.onRebind(intent);
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG,"onDestroy");
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d(TAG,"onUnbind");
        return super.onUnbind(intent);
    }
}
