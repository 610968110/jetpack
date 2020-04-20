/*
 * Copyright (c) 2019 Huami Inc. All Rights Reserved.
 */
package com.example.jetpackdemo.room

import androidx.room.ColumnInfo
import androidx.room.DatabaseView

/**
 * Author: liboxin
 * Email: liboxin@huami.com
 * Date: 2020/4/20
 * Time: 14:41
 * Desc:
 */
@DatabaseView("SELECT b.title as bookName,b.author_id as id,a.name as authorName FROM Book as b INNER JOIN author AS a ON b.author_id = a.id")
data class BookView(
    @ColumnInfo(name = "id")
    val id: Int,
    @ColumnInfo(name = "bookName")
    val bookName: String,
    @ColumnInfo(name = "authorName")
    val authorName: String
)