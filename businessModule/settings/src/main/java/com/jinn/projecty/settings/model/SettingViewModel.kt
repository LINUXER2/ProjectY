package com.jinn.projecty.settings.model

import android.app.Activity
import android.app.Application
import android.content.*
import android.database.Cursor
import android.os.SystemClock
import android.view.View
import androidx.lifecycle.*
import com.jinn.projecty.base.BaseModel
import com.jinn.projecty.base.BaseViewModel
import com.jinn.projecty.databases.AppDatabase
import com.jinn.projecty.databases.entity.StudentEntity
import com.jinn.projecty.databases.provider.MyAsyncQueryHandler
import com.jinn.projecty.databases.provider.MyContentProvider
import com.jinn.projecty.frameapi.base.BaseApplication
import com.jinn.projecty.settings.api.SettingRepo
import com.jinn.projecty.settings.ktx.launch
import com.jinn.projecty.utils.AssetUtils
import com.jinn.projecty.utils.LogUtils
import dalvik.system.DexClassLoader
import dalvik.system.DexFile
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.io.File
import java.lang.reflect.Constructor
import kotlin.system.measureTimeMillis

class SettingViewModel(application: Application) : BaseViewModel<BaseModel>(application) {
    private val repo by lazy {
        SettingRepo()
    }

    companion object {
        private const val TAG = "SettingViewModel"
    }

    private val mStudentDao by lazy { AppDatabase.getDatabase().studentDao() }
    private var mStudentListLiveData = MutableLiveData<List<StudentEntity>>()
    private var mListData = MutableLiveData<List<VideoBeanItem>>()

    fun getStudentLiveData(): LiveData<List<StudentEntity>> {
        return mStudentListLiveData
    }

    fun getListLiveData(): LiveData<List<VideoBeanItem>> {
        return mListData
    }

    /** 协程
     * https://juejin.cn/post/6987724340775108622
     * lambda语法
     * https://juejin.cn/post/7283776161528545317?searchId=20241220134554EE1AEE44186D5A09606C
     */
    fun insertStudentData() {
        launch {
            withContext(Dispatchers.IO) {
                val time = measureTimeMillis {
                    val job1 = async(Dispatchers.Default) {
                        LogUtils.d(TAG, "insertStudentData1")
                        delay(1000)
                        LogUtils.d(TAG, "insertStudentData2")
                        mStudentDao.insert(StudentEntity("張三" + SystemClock.elapsedRealtime(), "男", 18))
                    }
                    job1.await() // 用async和await实现串行任务
                    val job2 = async(Dispatchers.Default) {
                        LogUtils.d(TAG, "insertStudentData3")
                        delay(500)
                        LogUtils.d(TAG, "insertStudentData4")
                    }
                }
                LogUtils.d(TAG, "time cost :${time}")
            }

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
    fun queryContentProvider() {
        launch {
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

    /**
     *  网络请求，返回liveData
     */
    fun getServerData() {
        launch {
            val rsp = repo.getDataFromServer()
            LogUtils.d(TAG, "getServerData:$rsp")
            if (rsp.data != null && rsp.isSucceed()) {
                mListData.postValue(rsp.data!!)
            }
        }
    }


    /**
     *  网络请求，返回Observable
     */
    fun getServerData2() {
        repo.getDataFromServer2().subscribeOn(Schedulers.io()).subscribe(object :
            Observer<VideoBeanItem?> {
            override fun onSubscribe(d: Disposable) {
                LogUtils.d(TAG, "onSubscribe,")
            }

            override fun onNext(recommandData: VideoBeanItem) {
                LogUtils.d(TAG, "onNext:")

            }

            override fun onError(e: Throwable) {
                LogUtils.d(TAG, "onError,$e")
            }

            override fun onComplete() {
                LogUtils.d(TAG, "onComplete")
            }
        })
    }

    fun getRemoteView(activity: Activity) {
        launch {
            withContext(Dispatchers.IO) {
                AssetUtils.copyFileFromAssets(activity, "app-debug.apk")
            }
            val path = activity.cacheDir
                .toString() + "/" + "app-debug.apk"
            val dexClassLoader = DexClassLoader(path, null, null, activity.classLoader)
            try {
                val mLoadedClass = dexClassLoader.loadClass("com.jinn.plugin.ViewManager")
                val loadInstance = mLoadedClass.newInstance()
                val method = mLoadedClass.getMethod("getView", Context::class.java)
                val object1 = method.invoke(loadInstance, activity.application)
                if (object1 is View) {
                    LogUtils.i(TAG, "view loaded")
                }
                mLoadedClass.getMethod("onVisible").invoke(loadInstance)
            } catch (e: java.lang.Exception) {
                LogUtils.i(TAG, "load class error:$e")
            }
        }
    }

    fun patchClassLoader(cl: ClassLoader?, optDexFile: File, context: Context) {
        LogUtils.d(
            TAG, "patchClassloader, need load apk file exists： " + optDexFile.exists() + ", " +
                    "path: " + optDexFile.absolutePath
        )
        try {
            // 获取 BaseDexClassLoader : pathList
            val pathListField =
                DexClassLoader::class.java.superclass.getDeclaredField("pathList")
            pathListField.isAccessible = true
            val pathListObj = pathListField[cl]

            // 获取 PathList: Element[] dexElements
            val dexElementArray = pathListObj.javaClass.getDeclaredField("dexElements")
            dexElementArray.isAccessible = true
            val dexElements = dexElementArray[pathListObj] as Array<Any>

            // Element 类型
            val elementClass = dexElements.javaClass.componentType

            // 创建一个数组, 用来替换原始的数组
            val newElements = java.lang.reflect.Array.newInstance(
                elementClass, dexElements.size
                        + 1
            ) as Array<Any>

            // 构造插件Element(File file, boolean isDirectory, File zip, DexFile dexFile) 这个构造函数
            var constructor: Constructor<*>? = null
            var o: Any? = null
            try {
                constructor = elementClass.getConstructor(DexFile::class.java, File::class.java)
                o = constructor.newInstance(
                    DexFile.loadDex(
                        optDexFile.absolutePath,
                        context.getDir("outputDir", 0).absolutePath, 0
                    ), null
                )
            } catch (e: NoSuchMethodException) {
                constructor = elementClass.getConstructor(
                    File::class.java, Boolean::class.javaPrimitiveType, File::class.java,
                    DexFile::class.java
                )
                o = constructor.newInstance(
                    File("/system/lib64"), true, null, DexFile
                        .loadDex(
                            optDexFile.absolutePath,
                            context.getDir("outputDir", 0).absolutePath, 0
                        )
                )
            }
            val toAddElementArray = arrayOf(o)
            // 把原始的elements复制进去
            System.arraycopy(dexElements, 0, newElements, 0, dexElements.size)
            // 插件的那个element复制进去
            System.arraycopy(
                toAddElementArray, 0, newElements, dexElements.size,
                toAddElementArray.size
            )

            // 替换
            dexElementArray[pathListObj] = newElements
            LogUtils.i(TAG, "dexPathList: $pathListObj")
        } catch (e: java.lang.Exception) {
            LogUtils.d(TAG, "patch loaded apk fail:$e")
        }
    }
}