package com.example.jetpackdemo.room

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RoomActivity : AppCompatActivity() {

    private lateinit var idView: EditText
    private lateinit var nameView: EditText
    private lateinit var ageView: EditText
    private lateinit var dao: UserDao
    private lateinit var bookDao: BookDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        idView = findViewById(R.id.et_id)
        nameView = findViewById(R.id.et_name)
        ageView = findViewById(R.id.et_age)
        dao = AppDatabase.getInstance(this).userDao()
        bookDao = AppDatabase.getInstance(this).bookDao()
    }

    fun insert(view: View) {
        if (nameView.text.isNotEmpty() && ageView.text.isNotEmpty()) {
            val user = User()
            user.id = idView.text.toString().toInt()
            user.name = nameView.text.toString()
            user.age = ageView.text.toString().toInt()
            try {
                dao.insertAll(user)
            } catch (e: Exception) {
                Log.e("xys", "插入失败")
                e.printStackTrace()
                return
            }
            idView.setText("")
            nameView.setText("")
            ageView.setText("")
            val users = dao.getAll1()
            Log.e("xys", "插入成功 当前有${users.size}条数据")
            users.forEach {
                Log.e("xys", it.name)
            }
        } else {
            Toast.makeText(this, "may be null", Toast.LENGTH_SHORT).show()
        }
    }

    fun findAll(view: View) {
        val users = dao.getAll1()
        users.forEach {
            Log.e("xys", it.name)
        }
    }

    fun add(view: View) {
        for (i in 1..10) {
            val id = (Math.random() * 100000).toInt()
            Log.e("xys", "id:${id}")
            val user = User()
            user.id = id.toString().toInt()
            user.name = id.toString()
            user.age = id.toString().toInt()
            try {
                dao.insertAll(user)
            } catch (e: Exception) {
                Log.e("xys", "插入失败")
                e.printStackTrace()
            }
        }
        val users = dao.getAll1()
        Log.e("xys", "当前有${users.size}条数据")
    }

    /**
     * 级联插入
     */
    fun add1(view: View) {
        GlobalScope.launch {
            bookDao.addAuthor(Author(id = 0, name = "张胜男", age = 17, sex = "未知"))
            bookDao.addAuthor(Author(id = 1, name = "张胜男baba", age = 170, sex = "未知"))
            Log.e("xys", "${bookDao.findAllAuthor()}")

            bookDao.addBook(Book(title = "金瓶梅", authorId = 0))
            Log.e("xys", "${bookDao.findAllBook()}")
        }
    }

    /**
     * 级联修改
     */
    fun update1(view: View) {
        // 没法写，因为外键值主键id，up后就添加新的了，这里需要改成其他外键，略~
    }

    /**
     * 级联删除
     */
    fun del1(view: View) {
        bookDao.delAllAuthor()
        Log.e("xys", "book:${bookDao.findAllAuthor()}")
        Log.e("xys", "author:${bookDao.findAllBook()}")
    }

    /**
     * 级联删视图
     */
    fun view1(view: View) {
//        Log.e("xys", "${bookDao.findAllBookView()}")
    }
}
