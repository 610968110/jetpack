package com.example.jetpackdemo

import android.app.Application
import com.example.jetpackdemo.koin.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module

/**
 * Copyright © 2013-2019 Worktile. All Rights Reserved.
 * Author: Huami
 * Email: Huami@worktile.com
 * Date: 2019/10/11
 * Time: 10:15
 * Desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        val appModule = listOf(
            module {
                viewModel { TestViewModel() }
                single { SingleTestViewModel(get(), "tag") }
//                factory { TestViewModel() } bind Action1::class bind Action2::class
                // 自己控制生命周期
                scope(named("tp1")) {
                    scoped { ScopeViewModule() }
                }
                scope(named<TestActivity>()) {
                    scoped {
                        Scope2ViewModule()
                    }
                }
                factory { InjectViewModule() }
            }
        )
        // 1.X 这么写
//        startKoin(this, appModule, logger = AndroidLogger())
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(appModule)
        }
    }
}