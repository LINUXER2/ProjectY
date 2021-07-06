package com.jinn.projecty.main.message;

import android.os.Handler;
import android.os.Message;

import com.jinn.projecty.utils.LogUtils;

import java.lang.ref.PhantomReference;

/**
 * Created by jinnlee on 2021/7/3.
 */

public class MyHandler extends Handler {
    public static final int MSG_INIT = 1;
    private final String TAG = "MyHandler";

    public MyHandler() {
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case MSG_INIT:
                LogUtils.d(TAG,"msg_init,msgId:"+msg.obj);
                break;
            default:
                break;
        }
        super.handleMessage(msg);
    }
}
