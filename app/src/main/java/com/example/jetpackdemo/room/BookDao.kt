/*
 * Copyright (c) 2019 Huami Inc. All Rights Reserved.
 */
package com.example.jetpackdemo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Author: liboxin
 * Email: liboxin@huami.com
 * Date: 2020/4/20
 * Time: 11:03
 * Desc:
 */
@Dao
abstract class BookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addBook(book: Book)

    @Query("SELECT * FROM author")
    abstract fun findAllAuthor(): List<Author>

    @Query("SELECT * FROM BOOK")
    abstract fun findAllBook(): List<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun addAuthor(author: Author)

    @Update
    abstract fun updateAuthor(author: Author)

    @Query("DELETE FROM author")
    abstract fun delAllAuthor()

//    @Query("SELECT * FROM BookView")
//    abstract fun findAllBookView(): List<BookView>
}