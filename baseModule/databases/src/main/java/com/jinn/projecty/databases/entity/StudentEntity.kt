package com.jinn.projecty.databases.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "student",primaryKeys = ["name"])
data class StudentEntity(
    @ColumnInfo
    var name:String,
    @ColumnInfo
    var sex:String,
    @ColumnInfo(name="student_age")
    var age:Int
){

}