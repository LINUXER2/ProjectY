package com.jinn.projecty.databases.dao

import androidx.room.Dao
import androidx.room.Query
import com.jinn.projecty.databases.entity.StudentEntity

@Dao
interface StudentDao {
    @Query("SELECT * FROM student ORDER BY student_age")
    suspend fun queryAll():MutableList<StudentEntity>
}