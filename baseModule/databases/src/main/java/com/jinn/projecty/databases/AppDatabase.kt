package com.jinn.projecty.databases

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jinn.projecty.databases.dao.StudentDao
import com.jinn.projecty.databases.entity.StudentEntity
import com.jinn.projecty.frameapi.base.BaseApplication
import com.jinn.projecty.utils.LogUtils

/**
 * 数据库操作类
 */
@Database(entities = [StudentEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        const val TAG = "AppDatabase"

        fun getDatabase(): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    BaseApplication.sInstance, AppDatabase::class.java, "student.db"
                )
                    //是否允许在主线程查询，默认是false
                    //.allowMainThreadQueries()
                    //数据库被创建或者被打开时的回调
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            LogUtils.d(TAG, "onCreate")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            LogUtils.d(TAG, "onOpen")
                        }
                    })
                    //指定数据查询的线程池，不指定会有个默认的
                    //.setQueryExecutor {  }
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}