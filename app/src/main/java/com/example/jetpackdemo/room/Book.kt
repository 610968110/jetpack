package com.example.jetpackdemo.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Author: liboxin
 * Date: 2020/4/20
 * Time: 10:59
 * Desc:
 */

@Entity(
//    indices = [
//        Index("title")
//    ],
    foreignKeys = [
        ForeignKey(
            entity = Author::class,
            parentColumns = ["id"],
            childColumns = ["author_id"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Book(
    @PrimaryKey(autoGenerate = true)
    var bookId: Int = 0,
    var title: String,
    @ColumnInfo(name = "author_id")
    var authorId: Int
)