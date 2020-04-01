package com.example.jetpackdemo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.build.Msg.Companion.MSG
import com.example.jetpackdemo.channel.ChannelActivity
import com.example.jetpackdemo.coroutines.CoroutinesActivity
import com.example.jetpackdemo.koin.TestActivity
import com.example.jetpackdemo.livedata.LiveDataActivity
import com.example.jetpackdemo.navigation.HostActivity
import com.example.jetpackdemo.paged.PagedActivity
import com.example.jetpackdemo.room.RoomActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, MSG, Toast.LENGTH_SHORT).show()
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

    fun liveData(view: View) {
        startActivity(Intent(this, LiveDataActivity::class.java))
    }

    fun navigation(view: View) {
        startActivity(Intent(this, HostActivity::class.java))
    }

    fun launch(view: View) {
        startActivity(Intent(this, CoroutinesActivity::class.java))
    }

    fun channel(view: View) {
        startActivity(Intent(this, ChannelActivity::class.java))
    }
}

