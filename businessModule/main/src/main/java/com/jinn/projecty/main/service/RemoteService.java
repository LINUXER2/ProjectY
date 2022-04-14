package com.jinn.projecty.main.service;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

import com.jinn.projecty.utils.HeavyWorkThread;
import com.jinn.projecty.utils.LogUtils;

public class RemoteService extends Service {
    private final String TAG = "RemoteService";
    private Messenger mClientMessenger; //回复给client端的messenger
    public RemoteService() {
    }

    private Messenger mRemoteMessenger = new Messenger(new Handler(HeavyWorkThread.getLooper()){
        @Override
        public void handleMessage(Message msg) {
            LogUtils.d(TAG,"get message from client:"+msg.what);
            switch (msg.what){
                case 1:
                    mClientMessenger = msg.replyTo;
                    try{
                        Message message = Message.obtain();
                        message.what = 1;
                        Bundle bundle =new Bundle();
                        bundle.putString("reply_data","aaaa");
                        message.setData(bundle);
                        mClientMessenger.send(message);
                    }catch (Exception e){
                       LogUtils.e(TAG,"reply to client error:"+e.toString());
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    });

    @Override
    public IBinder onBind(Intent intent) {
         return mRemoteMessenger.getBinder();
    }
}