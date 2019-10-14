package com.example.jetpackdemo.koin

import android.util.Log
import androidx.lifecycle.ViewModel

/**
 * Copyright © 2013-2019 Worktile. All Rights Reserved.
 * Author: Huami
 * Email: Huami@worktile.com
 * Date: 2019/10/11
 * Time: 11:11
 * Desc:
 */
class TestViewModel : ViewModel(), Action1, Action2 {
    override fun action1() {
        Log.e("xys", "action1")
    }

    override fun action2() {
        Log.e("xys", "action2")
    }

    init {
        Log.e("xys", "TestViewModel init")
    }
}