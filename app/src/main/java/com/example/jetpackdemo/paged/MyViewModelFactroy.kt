package com.example.jetpackdemo.paged

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Copyright © 2013-2019 Worktile. All Rights Reserved.
 * Author: Huami
 * Email: Huami@worktile.com
 * Date: 2019/10/10
 * Time: 17:40
 * Desc:
 */
class MyViewModelFactroy(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PagedViewModel(context) as T
    }
}