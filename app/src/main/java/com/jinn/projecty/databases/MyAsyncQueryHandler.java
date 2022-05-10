package com.jinn.projecty.databases;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.jinn.projecty.utils.LogUtils;

public class MyAsyncQueryHandler extends AsyncQueryHandler {
    private final String TAG ="MyAsyncQueryHandler";
    public MyAsyncQueryHandler(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected Handler createHandler(Looper looper) {
        return super.createHandler(looper);
    }

    @Override
    public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
        super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        LogUtils.d(TAG,"onQueryComplete:"+token);
        super.onQueryComplete(token, cookie, cursor);
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        LogUtils.d(TAG,"onInsertComplete:"+token);
        super.onInsertComplete(token, cookie, uri);
    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        LogUtils.d(TAG,"onUpdateComplete:"+token);
        super.onUpdateComplete(token, cookie, result);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
        LogUtils.d(TAG,"onDeleteComplete:"+token);
        super.onDeleteComplete(token, cookie, result);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }
}
