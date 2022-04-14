package com.jinn.projecty.main.model;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import com.jinn.projecty.utils.HeavyWorkThread;
import com.jinn.projecty.utils.LogUtils;

/**
 *  负责与其它进程通信（client）
 *   clinet 端与service 端分别持有两个messenger，实现双向通信
 * Created by jinnlee on 2022/4/14.
 */

public class RemoteDataManager {

    private static RemoteDataManager sInstance;
    private Messenger mRemoteMessenger = null;    //向remote端发起数据
    private boolean isCollected = false;
    private Context mContext;
    private Runnable mWaitingRunnable = null;  // 当发送消息时，若service未连接，则保存该消息，待下次连接后重新发送
    private final String TAG = "RemoteDataManager";

    private RemoteDataManager(Context context){
         mContext = context;
    }

    public static RemoteDataManager getInstance(Context context){
        if(sInstance==null){
            synchronized (RemoteDataManager.class){
                if(sInstance==null){
                    sInstance = new RemoteDataManager(context);
                }
           }
        }
        return sInstance;
    }

    // 接收remote端傳來的数据
    private Messenger mClientMessenger = new Messenger(new Handler(HeavyWorkThread.getLooper()){
        @Override
        public void handleMessage(Message msg) {
            LogUtils.d(TAG,"get message from remote:"+msg.what);
            Bundle bundle =msg.getData();
            super.handleMessage(msg);
        }
    });

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.d(TAG,"onServiceConnected,"+name.toString());
            mRemoteMessenger = new Messenger(service);
            isCollected =true;
            if(mWaitingRunnable!=null){
                mWaitingRunnable.run();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            LogUtils.d(TAG,"onServiceDisconnected,"+name.toString());
            isCollected = false;
            mRemoteMessenger = null;
            mWaitingRunnable = null;
        }
    };

    public void sendMessage(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(mRemoteMessenger!=null){
                   Message message = Message.obtain();
                   message.what = 1;
                   Bundle bundle = new Bundle();
                   bundle.putString("key","value_0");
                   message.setData(bundle);
                   message.replyTo = mClientMessenger;
                   try {
                       mRemoteMessenger.send(message);
                   }catch (Exception e){
                       LogUtils.e(TAG,"send message erroe:"+e.toString());
                   }
                }
            }
        };
        if(!isCollected){
            mWaitingRunnable = runnable;
            connectToService();
        }else{
            runnable.run();
        }

    }

    public void connectToService(){
       LogUtils.d(TAG,"connectToService,"+isCollected);
      if(isCollected){
          return;
      }
      Intent intent = new Intent();
      intent.setPackage("com.jinn.projecty");
      intent.setAction("com.jinn.projecty.remoteservice");
      mContext.bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void disconnectService(){
        LogUtils.d(TAG,"disconnectService");
        if(!isCollected){
            return;
        }
        mContext.unbindService(mServiceConnection);
    }


}
