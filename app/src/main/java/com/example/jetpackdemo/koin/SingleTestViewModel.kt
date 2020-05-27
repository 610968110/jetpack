package com.example.jetpackdemo.koin

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * Date: 2019/10/11
 * Time: 11:11
 * Desc:
 */
class SingleTestViewModel(context: Context, tag: String) : ViewModel() {

    init {
        Log.e("xys", "SingleTestViewModel init context = $context   tag = $tag")
    }
}