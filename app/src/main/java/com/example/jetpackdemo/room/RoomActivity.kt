package com.example.jetpackdemo.room

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.R

class RoomActivity : AppCompatActivity() {

    private lateinit var idView: EditText
    private lateinit var nameView: EditText
    private lateinit var ageView: EditText
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)
        idView = findViewById(R.id.et_id)
        nameView = findViewById(R.id.et_name)
        ageView = findViewById(R.id.et_age)
        dao = AppDatabase.getInstance(this).userDao()
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

}
