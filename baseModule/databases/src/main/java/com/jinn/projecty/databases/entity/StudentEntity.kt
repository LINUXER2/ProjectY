package com.jinn.projecty.databases.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "student",primaryKeys = ["student_name"])
data class StudentEntity(
    @ColumnInfo(name ="student_name")
    var name:String,
    @ColumnInfo(name="student_sex")
    var sex:String,
    @ColumnInfo(name="student_age")
    var age:Int
){

}