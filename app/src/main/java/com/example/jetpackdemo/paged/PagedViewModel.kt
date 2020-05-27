package com.example.jetpackdemo.paged

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.jetpackdemo.room.AppDatabase
import com.example.jetpackdemo.room.User

/**
 * Date: 2019/10/10
 * Time: 15:16
 * Desc:
 */
class PagedViewModel(context: Context) : ViewModel() {
    var concertList: LiveData<PagedList<User>>
    private val dao = AppDatabase.getInstance(context).userDao()

    init {
        Log.e("xys", "PagedViewModel init")
        val myPagingConfig = PagedList.Config.Builder() // 分页设置
            .setPageSize(5) //配置分页加载的数量
            .setPrefetchDistance(150) // 预取距离，给定UI中最后一个可见的Item，超过这个item应该预取一段数据
            .setEnablePlaceholders(true) //配置是否启动PlaceHolders
            .setInitialLoadSizeHint(5) //初始化加载的数量
            .build()
        concertList = LivePagedListBuilder(dao.getAll(), myPagingConfig)
//            .setFetchExecutor(myExecutor)
            .build()
    }

    fun findAll(context: Context) {
        //传入Room返回的DataSource.Factory
        var liveArray: LiveData<PagedList<User>> =
            LivePagedListBuilder(
                dao.getAll(), PagedList.Config.Builder()
                    .setPageSize(10)
                    .setPrefetchDistance(10)
                    .setEnablePlaceholders(true)
                    .build()
            ).build()
    }

    fun textAddData(user: User) {
//        concertList.value?.dataSource.
    }
}