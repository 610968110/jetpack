package com.example.jetpackdemo.livedata

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import com.example.jetpackdemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class LiveDataActivity : AppCompatActivity() {

    private val data = MutableLiveData<Int>()
    private val data1 = MutableLiveData<String>()
    private val mdata = MediatorLiveData<String>()
    private val pool = ScheduledThreadPoolExecutor(1)
    private var i = 0
    private val vm: MyViewModel = MyViewModel()

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
//            data.postValue(i)
//            data1.postValue(i.toString())
        }, 1, 1, TimeUnit.SECONDS)
        vm.result.observe(this) {
            Log.e("xys", "result:${it}")
        }
        vm.currentWeatherFlow.observe(this) {
            Log.e("xys", "currentWeatherFlow:${it}")
        }
    }

    override fun onDestroy() {
        pool.shutdown()
        super.onDestroy()
    }
}

class MyViewModel {
    private val dataSource: DataSource = DataSource()

    val result = liveData {
        emit(doComputation())
    }

    private var i = 0
    fun doComputation(): Int = i++


    val currentWeatherFlow: LiveData<List<UserVo>> =
        dataSource.fetchWeatherFlow().asLiveData()
}

class DataSource {
    fun fetchWeatherFlow(): Flow<List<UserVo>> = flow {
        emit(listOf(UserVo("张三", 18), UserVo("李四", 19)))
    }
}

data class UserVo(val name: String, val age: Int)

