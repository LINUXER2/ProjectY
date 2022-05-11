package com.jinn.projecty.settings.server

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.jinn.projecty.settings.IMyAidlInterface
import com.jinn.projecty.utils.LogUtils

open class AidlService() : Service() {
    companion object{
        const val TAG ="AidlService"
    }


    private var mBinder =object: IMyAidlInterface.Stub(){
        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
           LogUtils.d(TAG,"basicTypes")
        }

        override fun getDataFromServer(): String {
            LogUtils.d(TAG,"getDataFromServer")
            return "this is a message from remote"
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
          return mBinder
    }
}