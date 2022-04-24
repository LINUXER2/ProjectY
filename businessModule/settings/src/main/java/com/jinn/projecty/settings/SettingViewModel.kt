package com.jinn.projecty.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jinn.projecty.databases.AppDatabase
import com.jinn.projecty.databases.dao.StudentDao

class SettingViewModel(application: Application) : AndroidViewModel(application) {
    private val mDataBase by lazy { AppDatabase.getDatabase() }
    private val mStudentDao:StudentDao  = mDataBase.studentDao()
    suspend fun getStudentDataFromDb(){
       val list =  mStudentDao.queryAll()

    }
}