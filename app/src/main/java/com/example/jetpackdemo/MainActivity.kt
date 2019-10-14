package com.example.jetpackdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.koin.TestActivity
import com.example.jetpackdemo.livedata.LiveDataActivity
import com.example.jetpackdemo.paged.PagedActivity
import com.example.jetpackdemo.room.RoomActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun paged(view: View) {
        startActivity(Intent(this, PagedActivity::class.java))
    }

    fun room(view: View) {
        startActivity(Intent(this, RoomActivity::class.java))
    }

    fun koin(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun livedata(view: View) {
        // 1000
        startActivity(Intent(this, LiveDataActivity::class.java))
    }
}
