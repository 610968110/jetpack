package com.example.jetpackdemo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Copyright © 2013-2019 Worktile. All Rights Reserved.
 * Author: Huami
 * Email: Huami@worktile.com
 * Date: 2019/10/10
 * Time: 15:28
 * Desc:
 * @ForeignKey   定义外键约束
 */
@Entity(
    tableName = "author"
)
data class Author(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String = "",
    @ColumnInfo(name = "age")
    var age: Int = 0,
    @Ignore // 不是忽略这个字段，而是忽略这个字段的赋值
    var sex: String = "不知道啊"
)