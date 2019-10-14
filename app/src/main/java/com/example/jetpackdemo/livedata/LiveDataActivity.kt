package com.example.jetpackdemo.livedata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.jetpackdemo.R
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class LiveDataActivity : AppCompatActivity() {

    private val data = MutableLiveData<Int>()
    private val data1 = MutableLiveData<String>()
    private val mdata = MediatorLiveData<String>()
    private val pool = ScheduledThreadPoolExecutor(1)
    private var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)
        data.value = i
        data1.value = i.toString()
        mdata.addSource(data) {
            mdata.value = it.toString()
        }
        mdata.addSource(data1) {
            mdata.value = it
        }
        mdata.observe(this, Observer {
            Log.e("xys", "o:${it}")
        })
        pool.scheduleAtFixedRate({
            i++
            data.postValue(i)
            data1.postValue(i.toString())
        }, 1, 1, TimeUnit.SECONDS)
    }

    override fun onDestroy() {
        pool.shutdown()
        super.onDestroy()
    }
}
