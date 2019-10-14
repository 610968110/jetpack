package com.example.jetpackdemo.paged

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jetpackdemo.R
import com.example.jetpackdemo.room.AppDatabase
import com.example.jetpackdemo.room.UserDao

class PagedActivity : AppCompatActivity() {

    private lateinit var view: RecyclerView
    private lateinit var dao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paged)

        dao = AppDatabase.getInstance(this).userDao()

        view = findViewById(R.id.rv_main)
        view.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

//        val pagedViewModel = ViewModelProviders.of(this)[PagedViewModel::class.java]
        val pagedViewModel = ViewModelProviders.of(
            this,
            MyViewModelFactroy(this)
        ).get(PagedViewModel::class.java)
        Log.e("xys", "pagedViewModel:${pagedViewModel}")

        val adapter = UserAdapter()
        view.adapter = adapter
        pagedViewModel.concertList.observe(this, Observer {
            Log.e("xys", "it.size:${it.size}")
            adapter.submitList(it)
        })
    }

    fun change(view: View) {
        dao.delete(dao.getAll1()[0])
    }
}
