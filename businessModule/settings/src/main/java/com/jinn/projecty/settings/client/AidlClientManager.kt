package com.jinn.projecty.settings.client

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.jinn.projecty.settings.IMyAidlInterface
import com.jinn.projecty.utils.LogUtils

object AidlClientManager{

    const val TAG ="AidlClientManager"
    private var mBinder:IMyAidlInterface?=null
    private var isConnected = false
    /**
     * 绑定AIDL服务端
     */
    fun bindService(context: Context){
        if(!isConnected){
            val intent = Intent("com.jinn.projecty.AidlService")
            intent.setPackage("com.jinn.projecty")
            context.bindService(intent,mConnection, Context.BIND_AUTO_CREATE)
        }
    }

    private val mConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, iBinder: IBinder?) {
            LogUtils.d(TAG,"onServiceConnected")
            mBinder = IMyAidlInterface.Stub.asInterface(iBinder)
            isConnected = true
            mBinder?.dataFromServer
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            LogUtils.d(TAG,"onServiceDisconnected")
            mBinder = null
            isConnected = false
        }
    }
}
