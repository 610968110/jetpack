package com.example.jetpackdemo.workmanager

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.jetpackdemo.R

class WorkManagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager)
        // PeriodicWorkRequestBuilder 周期任务 周期间隔最少为10s ，  OneTimeWorkRequestBuilder 单次任务
//        val work = PeriodicWorkRequestBuilder<MyWorker>(10, TimeUnit.SECONDS)
        val work = OneTimeWorkRequestBuilder<MyWorker>()
            .addTag("A") // A组。这样的好处是以后可以直接控制整个组就行了，组内的每个成员都会受到影响。
            .setConstraints(
                Constraints.Builder().run {
//                    setRequiresBatteryNotLow()// 执行任务时电池电量不能偏低。
//                    setRequiresCharging()// 在设备充电时才能执行任务。
//                    setRequiresDeviceIdle()// 设备空闲时才能执行。
//                    setRequiresStorageNotLow()// 设备储存空间足够时才能执行。
                    build()
                }
            )
            .build()

        val liveData = WorkManager.getInstance().getWorkInfoByIdLiveData(work.id)
        liveData.observe(this, Observer {
//            ENQUEUED,//已加入队列
//            RUNNING,//运行中
//            SUCCEEDED,//已成功
//            FAILED,//已失败
//            BLOCKED,//已刮起
//            CANCELLED;//已取消
            Log.e("xys", "liveData:${it}")
        })

        // 对于单个的WorkRequest，可以直接通过WorkManager的enqueue方法
        WorkManager.getInstance().enqueue(work)
        // 想使用链式工作，只需调用beginWith或者beginUniqueWork方法即可
        val workContinuation = WorkManager.getInstance().beginWith(work).then(work).enqueue()
//        val workContinuation =  WorkManager.getInstance().beginUniqueWork(Constants.IMAGE_UNIQUE_WORK, ExistingWorkPolicy.REPLACE, cleanUpWork).then(work).enqueue()


//        WorkManager.getInstance().cancelWorkById(work.id)
        // cancelAllWork():取消所有任务。
        //cancelAllWorkByTag(tag:String):取消一组带有相同标签的任务。
        //cancelUniqueWork(uniqueWorkName:String):取消唯一任务。
    }
}

class MyWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    companion object {
        private const val KEY: String = "key_accept_bg_work"
    }


    override fun doWork(): Result {
        Log.e("xys", "doWork ${Thread.currentThread().name}")
        // 模拟设置页面中的"是否接受推送"是否被勾选
        val isOkay = inputData.getBoolean(KEY, true)
        return if (isOkay) {
            Thread.sleep(1000) //模拟长时间工作
            val pulledResult = "pulledResult success"
            val output = Data.Builder()
                .putString(KEY, pulledResult)
                .build()
            Log.e("xys", "doWork success ${Thread.currentThread().name}")
            Result.success(output)
        } else {
            Result.failure()
        }
    }


    override fun onStopped() {
        super.onStopped()
        Log.e("xys", "onStopped ${Thread.currentThread().name}")
        // 当任务结束时会回调这里
    }
}