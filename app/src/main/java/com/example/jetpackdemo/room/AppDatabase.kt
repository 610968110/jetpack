package com.example.jetpackdemo.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [User::class, Book::class, Author::class],
    views = [BookView::class],
    version = 4,
    exportSchema = false
)
//@TypeConverters(Converters.class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookDao(): BookDao

    companion object {
        private var INSTANCE: AppDatabase? = null
        private val sLock = Any()
        fun getInstance(context: Context): AppDatabase {
            synchronized(sLock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "test_db.db"
                    )
                        .fallbackToDestructiveMigration() // 迁移，会清空数据库，否则升级版本号会crash，
                        .allowMainThreadQueries() //room默认数据库的查询是不能在主线程中执行的，除非这样设置
                        .build()
                }
                return INSTANCE!!
            }
        }
    }
}