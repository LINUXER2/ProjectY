package com.jinn.projecty.databases.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.jinn.projecty.databases.entity.StudentEntity

@Dao
interface StudentDao {
    /**
     * https://blog.csdn.net/weixin_44666188/article/details/105500779
     * 注意，使用liveData作为返回值时不需要开线程，room会自动切到线程中查询
     */
    @Query("SELECT * FROM student ORDER BY student_age")
    fun queryAllByLiveData():LiveData<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(element:StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list:MutableList<StudentEntity>)

    @Delete
    suspend fun delete(element: StudentEntity)

    @Delete
    suspend fun deleteList(list:MutableList<StudentEntity>)

    @Query("DELETE FROM student WHERE  student_name = (:name)")
    suspend fun deleteByName(name:String)

    @Query("DELETE FROM student WHERE student_name IN (:list)")
    suspend fun deleteByNames(list:List<String>)
}