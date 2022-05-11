package com.jinn.projecty.settings

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.SystemClock
import androidx.lifecycle.*
import com.jinn.projecty.databases.AppDatabase
import com.jinn.projecty.databases.entity.StudentEntity
import com.jinn.projecty.utils.LogUtils

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    companion object{
        private const val TAG ="SettingViewModel"
    }
    private val mStudentDao by lazy { AppDatabase.getDatabase().studentDao() }
    private var mStudentListLiveData = MutableLiveData<List<StudentEntity>>()

    fun getStudentLiveData():LiveData<List<StudentEntity>>{
        return mStudentListLiveData
    }

    fun insertData(){
        mStudentDao.insert(StudentEntity("張三"+SystemClock.elapsedRealtime(),"男",18))
    }

    /**
     * 监听数据变化
     */
    fun queryAll():LiveData<List<StudentEntity>>{
        return mStudentDao.queryAllByLiveData()
    }


}