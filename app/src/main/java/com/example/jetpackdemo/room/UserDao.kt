package com.example.jetpackdemo.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM User")
    fun getAll1(): List<User>


    @Query("SELECT * FROM User")
    fun getAll(): DataSource.Factory<Int, User>  // 返回DataSource.Factory配合PagingList使用

    @Query("SELECT * FROM User WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM User WHERE name LIKE :name ")
    fun findByName(name: String): User

    @Insert
    fun insertAll(user: User)

    @Delete
    fun delete(user: User)

    @Delete
    fun deleteAll(users: List<User>)

//    @Transaction //事务查询
//    @Query("SELECT uid, name from User")
//    List<UserAllVideos> loadUsers();
}

