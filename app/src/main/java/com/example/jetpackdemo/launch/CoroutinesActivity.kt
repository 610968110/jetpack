package com.example.jetpackdemo.launch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.system.measureTimeMillis

class CoroutinesActivity : AppCompatActivity() {

    private fun fooSleep(): Flow<Int> = flow {
        // 序列构建器
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们正在计算
            emit(i) // 产生下一个值
        }
    }

    private fun fooSequence(): Sequence<Int> = sequence {
        // 序列构建器
        for (i in 1..3) {
            Thread.sleep(100) // 假装我们正在计算
            yield(i) // 产生下一个值
        }
    }

    private fun fooDelay(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
        }
    }

    private fun fooString(): Flow<String> = flow {
        for (i in 1..3) {
            delay(100)
            emit("$i")
        }
    }

    private fun fooErr(errPos: Int = 2): Flow<String> = flow {
        for (i in 1..3) {
            delay(100)
            emit(i)
            // check(it == errPos) //  { "crash on pos $it" }
        }
    }.map {
        check(it != errPos) { "err on pos:$errPos value:$it" }
        "success:$it"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutines)
        GlobalScope.launch(Dispatchers.Main) {
            println("onCreate in")

//            test()

            logLine("异步流")
            fooSleep().collect { it.println() }

            logLine("序列 消耗cpu时用")
            fooSequence().forEach { it.println() }

            logLine("流取消 withTimeoutOrNull")
            withTimeoutOrNull(250) {
                // 在 250 毫秒后超时
                fooDelay().collect { it.println() }
            }

            logLine("流构建器 asFlow")
            (1..3).asFlow().collect { it.println() }

            logLine("过渡流操作符 map转换")
            (1..3).asFlow() // 一个请求流
                .map { request -> performRequest(request) }
                .collect { it.println() }

            logLine("转换操作符 同一值可发射多次")
            (1..3).asFlow() // 一个请求流
                .transform { request ->
                    emit("Making request $request")
                    emit(performRequest(request))
                }
                .collect { it.println() }

            logLine("限长操作符 reduce")
            fooSleep().take(2).collect { it.println() }

            logLine("末端流操作符 take")
            (1..5).asFlow()
                .map { it * it } // 数字 1 至 5 的平方
                .reduce { a, b -> a + b }
                .println()

            logLine("流的上下文 flowOn")
            fooSleep().flowOn(Dispatchers.IO).collect { it.println() }

            logLine("缓冲 buffer")
            val time = measureTimeMillis {
                fooDelay()
                    .buffer() // 缓冲发射项，无需等待
                    .collect { value ->
                        delay(300) // 假装我们花费 300 毫秒来处理它
                        println(value)
                    }
            }
            time.println()

            logLine("合并 conflate 忽略中间发射值")
            fooDelay()
                .conflate() // 合并发射项，不对每个值进行处理
                .collect { value ->
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println(value)
                }

            logLine("处理最新值 conflate")
            fooDelay()
                .collectLatest { value ->
                    // 取消并重新发射最后一个值
                    println("Collecting $value")
                    delay(300) // 假装我们花费 300 毫秒来处理它
                    println("Done $value")
                }

            logLine("Zip 以长时间为准")
            val nums = (1..3).asFlow() // 数字 1..3
            val strs = flowOf("one", "two", "three") // 字符串
            nums.zip(strs) { a, b -> "$a -> $b" } // 组合单个字符串
                .collect { println(it) } // 收集并打印

            logLine("Combine 每个流完成都回调 onEach 事后操作")
            val nums1 = (1..3).asFlow().onEach { delay(300) } // 发射数字 1..3，间隔 300 毫秒
            val strs1 = flowOf("one", "two", "three").onEach { delay(400) } // 每 400 毫秒发射一次字符串
            val startTime = System.currentTimeMillis() // 记录开始的时间
            nums1.combine(strs1) { a, b -> "$a -> $b" } // 使用“zip”组合单个字符串
                .collect { value ->
                    // 收集并打印
                    println("$value at ${System.currentTimeMillis() - startTime} ms from start")
                }

            logLine("展平流 利用流函数创造子流")
            (1..3).asFlow().map { requestFlow(it) }.collect { it.collect { it.println() } }

            /**
             * 1: First at 107 ms from start [main]
             * 1: Second at 609 ms from start [main]
             * 2: First at 711 ms from start [main]
             * 2: Second at 1213 ms from start [main]
             * 3: First at 1315 ms from start [main]
             * 3: Second at 1817 ms from start [main]
             */
            logLine("展平流拓展 流变换 flatMapConcat 每个子流顺序执行")
            val startTime1 = System.currentTimeMillis() // remember the start time
            (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
                .flatMapConcat { requestFlow(it) }
                .collect { value ->
                    // collect and print
                    println("$value at ${System.currentTimeMillis() - startTime1} ms from start")
                }

            /**
             *  1: First at 120 ms from start [main]
             *  2: First at 222 ms from start [main]
             *  3: First at 323 ms from start [main]
             *  1: Second at 622 ms from start [main]
             *  2: Second at 723 ms from start [main]
             *  3: Second at 827 ms from start [main]
             *  2: Third at 1230 ms from start [main]
             *  3: Third at 1332 ms from start [main]
             */
            logLine("展平流拓展 流变换  flatMapMerge 每个子流的同一pos发射项轮流执行")
            val startTime2 = System.currentTimeMillis() // remember the start time
            (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
                .flatMapMerge { if (it == 2 || it == 3) requestFlow2(it) else requestFlow(it) } // 创造不通发射值的流~
                .collect { value ->
                    // collect and print
                    println("$value at ${System.currentTimeMillis() - startTime2} ms from start")
                }

            /**
             * 1: First at 102 ms from start [main]
             * 2: First at 212 ms from start [main]
             * 3: First at 320 ms from start [main]
             * 3: Second at 822 ms from start [main]
             */
            logLine("展平流拓展 流变换  flatMapLatest 取消先前流的集合再执行最新的")
            val startTime3 = System.currentTimeMillis() // remember the start time
            (1..3).asFlow().onEach { delay(100) } // a number every 100 ms
                .flatMapLatest { requestFlow(it) }
                .collect { value ->
                    // collect and print
                    println("$value at ${System.currentTimeMillis() - startTime3} ms from start")
                }

            logLine("流异常 check 可打印自定义msg  java.lang.IllegalStateException: err on pos:2 value:2")
            kotlin.runCatching {
                fooErr(2).collect { it.println() }
            }.println()

            logLine("异常透明性  透明捕获 直接用.catch捕获")
            fooErr(2)
                .catch { err -> emit("crash啦 $err") } // emit on exception
                .collect { value -> println(value) }

            logLine("异常透明性  声明式捕获 onEach + .collect()")
            fooDelay()
                .onEach { value ->
                    check(value != 1) { "Collected $value" }
                    println(value)
                }
                .catch { e -> println("Caught $e") }
                .collect()

            logLine("命令式 finally 块   onCompletion")
            fooDelay().onCompletion { println("Done") }.collect { it.println() }
            fooErr(2).catch { emit("err -> $it") }.onCompletion { println("Done") }
                .collect { it.println() }

            logLine("在单独的携程中运行(异步)  launchIn")
            fooDelay().onEach { "onEach:$it".println() }.launchIn(this)
            fooDelay().onEach { "onEach1:$it".println() }.launchIn(this)

            println("onCreate out")
        }
    }

    private suspend fun performRequest(request: Int): String {
        delay(100) // 模仿长时间运行的异步工作
        return "response $request"
    }

    /**
     * 会阻塞
     */
    private fun test() = runBlocking(Dispatchers.IO) {
        println("runBlocking in")
        delay(1000)
        println("runBlocking out")
    }

    private fun requestFlow(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
    }

    private fun requestFlow2(i: Int): Flow<String> = flow {
        emit("$i: First")
        delay(500) // wait 500 ms
        emit("$i: Second")
        delay(500) // wait 500 ms
        emit("$i: Third")
    }

    private fun logLine(any: Any) {
        println("---------- $any ----------", false)
    }

    private fun println(any: Any, printThread: Boolean = false) {
        val threadName = if (printThread) {
            " [${Thread.currentThread().name}]"
        } else {
            " "
        }
        when (any) {
            is String, is Int -> {
                Log.e("xys", "$any$threadName")
            }
            else -> {
                Log.e("xys", "$any$threadName")
            }
        }
    }

    private fun Any.println() {
        println(this)
    }
}