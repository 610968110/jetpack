package com.example.jetpackdemo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Date: 2019/10/10
 * Time: 15:28
 * Desc:
 * @ForeignKey   定义外键约束
 */
@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "age")
    var age: Int = 0,
    @Ignore
    var sex: String = ""
)