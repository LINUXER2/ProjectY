package com.jinn.projecty.databases

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jinn.projecty.databases.dao.StudentDao
import com.jinn.projecty.databases.entity.StudentEntity
import com.jinn.projecty.frameapi.base.BaseApplication

@Database(entities = [StudentEntity::class],version = 1,exportSchema = false)
abstract class AppDatabase :RoomDatabase(){
    abstract fun studentDao():StudentDao

    companion object{
       @Volatile
       private var INSTANCE:AppDatabase?=null

        fun getDatabase():AppDatabase{
            return INSTANCE?: synchronized(this){
                val instance = Room.databaseBuilder(
                    BaseApplication.sInstance,
                    AppDatabase::class.java,
                    "projecty2.db"
                ).allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}