package com.jinn.projecty.databases.dao

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.jinn.projecty.databases.entity.StudentEntity

@Dao
interface StudentDao {
    /**
     * https://blog.csdn.net/weixin_44666188/article/details/105500779
     * https://www.csdn.net/tags/NtzaYg5sNzk0NTItYmxvZwO0O0OO0O0O.html
     * 注意，使用liveData作为返回值时不需要开线程，room会自动切到线程中查询
     */
    @Query("SELECT * FROM student ORDER BY student_age")
    fun queryAllByLiveData():LiveData<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(element:StudentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertList(list:MutableList<StudentEntity>)

    @Delete
    fun delete(element: StudentEntity)

    @Delete
    fun deleteList(list:MutableList<StudentEntity>)

    @Query("DELETE FROM student WHERE  student_name = (:name)")
    fun deleteByName(name:String)

    @Query("DELETE FROM student WHERE student_name IN (:list)")
    fun deleteByNames(list:List<String>)
}