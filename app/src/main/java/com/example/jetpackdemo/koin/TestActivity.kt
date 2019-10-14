package com.example.jetpackdemo.koin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.R
import org.koin.android.ext.android.get
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named

class TestActivity : AppCompatActivity() {

    private val vm: TestViewModel = get()
    private val svm: SingleTestViewModel = get()

//    private val svm1: Action1 = get()
//    private val svm2: Action2 = get()

    // 如果存在则直接获取，否则创建 scope
    val scope = getKoin().getOrCreateScope("my", named("tp1"))
    // stove1和stove2是同一个实例
    val stove1: ScopeViewModule = scope.get()
    val stove2: ScopeViewModule = scope.get()

    //    val stove22: Scope2ViewModule by viewModel()
//    val stove22: Scope2ViewModule = get()

    val inject: InjectViewModule by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
    }

    fun jump(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        // close后这里不会为null，下个页面再次创建后的实例不是次实例，地址会不一样
        Log.e("xys", "stove1:${stove1}")
        Log.e("xys", "stove2:${stove2}")
    }

    override fun onDestroy() {
        scope.close()
        super.onDestroy()
    }
}
