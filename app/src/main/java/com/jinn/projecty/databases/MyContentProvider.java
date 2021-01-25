package com.jinn.projecty.databases;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.jinn.projecty.BuildConfig;
import com.jinn.projecty.utils.LogUtils;

public class MyContentProvider extends ContentProvider {

    public static final String DB_NAME = "projecty.db";
    private static final int DB_VERSION = 1;
    //URI格式：[scheme:][//host:port][path][?query]
    public static final Uri USER = Uri.parse("content://com.jinn.projecty/user");
    public static final Uri GOODS = Uri.parse("content://com.jinn.projecty/goods");
    private static final String TAG = "MyContentProvider";

    //user 表
    public static final String DB_TABLE_USER = "user";
    public static final String DB_COLUMN_USER_ID = "_id";
    public static final String DB_COLUMN_USER_NAME = "name";
    public static final String DB_COLUMN_USER_SEX = "sex";
    public static final String DB_COLUMN_USER_AGE = "age";

    // goods 表
    public static final String DB_TABLE_GOODS = "goods";
    public static final String DB_COLUMN_GOODS_ID = "_id";
    public static final String DB_COLUMN_GOODS_TYPE = "type";


    private DBHelper mDBHelper;


    public MyContentProvider() {
    }

    @Override
    public boolean onCreate() {
        // provider 中的onCreate早于Application onCreate，早于activity onCreate
        //01-25 20:20:17.593  6012  6012 D BaseApplication: BaseApplication,attachBaseContext
        //01-25 20:20:17.597  6012  6012 D MyContentProvider: onCreate
        //01-25 20:20:17.606  6012  6012 D BaseApplication: BaseApplication,onCreate
        LogUtils.d(TAG, "onCreate");
        mDBHelper = new DBHelper(getContext());
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        return true;
    }


    @Override
    public String getType(Uri uri) {
        return "";
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null) {
            return 0;
        }
        SqlArguments arg = new SqlArguments(uri);
        int count = 0;
        try {
            count = db.delete(arg.table, selection, selectionArgs);
        } catch (Exception e) {
            LogUtils.e(TAG, "insert error:" + e.toString());
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null) {
            return null;
        }
        SqlArguments arg = new SqlArguments(uri);

        try {
            db.insert(arg.table, null, values);
        } catch (Exception e) {
            LogUtils.e(TAG, "insert error:" + e.toString());
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null) {
            return null;
        }
        //根据uri匹配表名： uri.getPathSegments().get(0)
        SqlArguments arg = new SqlArguments(uri, selection, selectionArgs);
        Cursor cursor = null;
        try {
            cursor = db.query(arg.table, projection, selection, selectionArgs, null, null, sortOrder);
        } catch (SQLiteDiskIOException e) {
            LogUtils.e(TAG, "query error:" + e.toString());
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db == null) {
            return 0;
        }

        SqlArguments arg = new SqlArguments(uri, selection, selectionArgs);
        int count = 0;
        try {
            count = db.update(arg.table, values, selection, selectionArgs);
        } catch (SQLiteDiskIOException e) {
            LogUtils.e(TAG, "update error:" + e.toString());
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }


    private class DBHelper extends SQLiteOpenHelper {
        private Context mContext;

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            mContext = context;
        }

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            LogUtils.d(TAG, "onCreate,start init db,"+Thread.currentThread().getName());
            //该方法在数据库首次创建时调用，即第1次调用 getWritableDatabase（） / getReadableDatabase（）时，运行在main进程
            createDatabases(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            LogUtils.d(TAG, "onUpgrade,old:" + oldVersion + ",new:" + newVersion);
        }
    }

    private void createDatabases(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE_USER + " (_id INTEGER PRIMARY KEY,name TEXT,sex TEXT ,age INTEGER DEFAULT 10)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + DB_TABLE_GOODS + " (_id INTEGER PRIMARY KEY,type TEXT)");
    }

    /**
     * 主要作用是通过uri获取表名，需要uri按照一定的规则定义
     */
    private static class SqlArguments {
        public String table;
        public String where;
        public String[] args;

        SqlArguments(Uri uri) {
            if (uri.getPathSegments().size() == 1) {
                this.table = uri.getPathSegments().get(0);
                this.where = null;
                this.args = null;
            } else {
                this.table = null;
                this.where = null;
                this.args = null;
                if (BuildConfig.DEBUG) {
                    throw new IllegalArgumentException("invalid uri:" + uri.toString());
                }
            }
        }

        SqlArguments(Uri uri, String where, String[] args) {
            if (uri.getPathSegments().size() == 1) {
                this.table = uri.getPathSegments().get(0);
                this.where = where;
                this.args = args;
            } else {
                this.table = uri.getPathSegments().get(0);
                this.where = "_id=" + ContentUris.parseId(uri);
                this.args = null;
                if (BuildConfig.DEBUG) {
                    throw new IllegalArgumentException("invalid uri:" + uri.toString());
                }
            }
        }

    }
}
