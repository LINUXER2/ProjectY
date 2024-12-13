package com.jinn.projecty.settings.model

import android.app.Application
import android.content.*
import android.database.Cursor
import android.os.SystemClock
import androidx.lifecycle.*
import com.jinn.projecty.base.BaseModel
import com.jinn.projecty.base.BaseViewModel
import com.jinn.projecty.databases.AppDatabase
import com.jinn.projecty.databases.entity.StudentEntity
import com.jinn.projecty.databases.provider.MyAsyncQueryHandler
import com.jinn.projecty.databases.provider.MyContentProvider
import com.jinn.projecty.frameapi.base.BaseApplication
import com.jinn.projecty.settings.ktx.launch
import com.jinn.projecty.utils.HeavyWorkThread
import com.jinn.projecty.utils.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SettingViewModel(application: Application) : BaseViewModel<BaseModel>(application) {
    companion object {
        private const val TAG = "SettingViewModel"
    }

    private val mStudentDao by lazy { AppDatabase.getDatabase().studentDao() }
    private var mStudentListLiveData = MutableLiveData<List<StudentEntity>>()

    fun getStudentLiveData(): LiveData<List<StudentEntity>> {
        return mStudentListLiveData
    }

    suspend fun insertStudentData() {
        withContext(Dispatchers.IO) {
            LogUtils.d(TAG, "insertStudentData1")
            delay(1000)
            LogUtils.d(TAG, "insertStudentData2")
            mStudentDao.insert(StudentEntity("張三" + SystemClock.elapsedRealtime(), "男", 18))
        }
    }

    /**
     * 监听数据变化
     */
    fun queryAllStudent(): LiveData<List<StudentEntity>> {
        return mStudentDao.queryAllByLiveData()
    }

    /**
     * 查询contentProvider
     */
    suspend fun queryContentProvider() {
        withContext(Dispatchers.IO) {
            val resolver: ContentResolver = BaseApplication.sInstance.contentResolver
            var cursor: Cursor? = null
            try {
                cursor = resolver.query(
                    MyContentProvider.USER,
                    arrayOf(
                        MyContentProvider.DB_COLUMN_USER_AGE,
                        MyContentProvider.DB_COLUMN_USER_NAME
                    ),
                    null,
                    null,
                    MyContentProvider.DB_COLUMN_USER_AGE + " DESC"
                )
                if (cursor != null && cursor.count > 0) {
                    val nameIndex =
                        cursor.getColumnIndexOrThrow(MyContentProvider.DB_COLUMN_USER_NAME)
                    while (cursor.moveToNext()) {
                        val name = cursor.getString(nameIndex)
                        LogUtils.d(TAG, "query user:$name")
                    }
                }
            } catch (e: Exception) {
                LogUtils.e(TAG, "error:$e")
            }
            cursor?.close()
        }


        //AsyncQueryHandler是一个异步的查询操作帮助类，可以处理增删改ContentProvider提供的数据并在主线程回调查询结果
        val queryHandler = MyAsyncQueryHandler(BaseApplication.sInstance.contentResolver)
        queryHandler.startQuery(
            0,
            null,
            MyContentProvider.USER,
            arrayOf<String>(
                MyContentProvider.DB_COLUMN_USER_AGE,
                MyContentProvider.DB_COLUMN_USER_NAME
            ),
            null,
            null,
            MyContentProvider.DB_COLUMN_USER_AGE + " DESC"
        )
    }

}