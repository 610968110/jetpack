package com.example.jetpackdemo.channel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jetpackdemo.R
import com.example.jetpackdemo.logLine
import com.example.jetpackdemo.println
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

class ChannelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_channel)

        GlobalScope.launch {

            /**
             * 基础
             */
            logLine("基础")
            val channel = Channel<Int>()
            launch {
                for (x in 1..5) {
                    println("channel.send:$x")
                    channel.send(x)
                }
                channel.close()
            }
            for (i in channel) i.println()
            // 执行三次的时候，send4次，第5次不会send了
//            repeat(3) {
//                channel.receive().println()
//            }
            "Done".println()


            /**
             * 构建通道生产者
             */
            logLine("构建通道生产者")
            fun CoroutineScope.produceSquares(): ReceiveChannel<Int> = produce {
                for (x in 1..5) {
                    println("send:${x * x}")
                    send(x * x)
                }
            }

            val squares = produceSquares()
            squares.consumeEach { println(it) }
            "Done".println()


            /**
             * 管道
             */
            logLine(
                "管道 " +
                        "管道是一种一个协程在流中开始生产可能无穷多个元素的模式，" +
                        "并且另一个或多个协程开始消费这些流，做一些操作，并生产了一些额外的结果"
            )
            fun CoroutineScope.produceNumbers(): ReceiveChannel<Int> = produce {
                var x = 1
                while (true) {
                    println("生产:${x * x}")
                    send(x++)
                }// 在流中开始从 1 生产无穷多个整数
            }

            fun CoroutineScope.square(numbers: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
                "numbers:$numbers".println()
                for (x in numbers) {
                    println("消费:${x * x}")
                    send(x * x)
                }
            }

            val numbers = produceNumbers() // 从 1 开始生成整数
            val squares1 = square(numbers) // 整数求平方
            repeat(5) {
                println("接收:${squares1.receive()}") // 输出前五个
            }
            "Done".println()
            coroutineContext.cancelChildren() // 取消子协程


            /**
             * 扇入 多个协程可以发送到同一个通道
             */
            logLine("扇入 多个协程可以发送到同一个通道")
            suspend fun sendString(channel: SendChannel<String>, s: String, time: Long) {
                while (true) {
                    delay(time)
                    channel.send(s)
                }
            }

            val channel1 = Channel<String>()
            // 2个协程发送生产
            launch { sendString(channel1, "foo", 200L) }
            launch { sendString(channel1, "BAR!", 500L) }
            // 一个通道接收
            repeat(6) {
                // 接收前六个
                println(channel1.receive())
            }
            coroutineContext.cancelChildren() // 取消所有子协程来让主协程结束


            /**
             * 扇出 多个协程接收相同的管道
             */
            logLine("扇出 多个协程接收相同的管道")
            // 1个发送生产
            fun CoroutineScope.produceNumbers1() = produce {
                var x = 1 // 从 1 开始
                while (true) {
                    send(x++) // 产生下一个数字
                    delay(100) // 等待 0.1 秒
                }
            }

            // 多个协程接收
            fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
                for (msg in channel) {
                    // 100ms打印一次
                    println("Processor #$id received $msg")
                }
            }

            val producer = produceNumbers1()
            repeat(5) { launchProcessor(it, producer) }
            delay(950)
            producer.cancel() // 取消协程生产者从而将它们全部杀死


            /**
             * 带缓冲的通道 只打印01234
             * 前四个元素被加入到了缓冲区并且发送者在试图发送第五个元素的时候被挂起
             */
            logLine("带缓冲的通道")
            val channel2 = Channel<Int>(4) // 启动带缓冲的通道
            val sender = launch {
                // 启动发送者协程
                repeat(10) {
                    println("Sending $it") // 在每一个元素发送前打印它们
                    channel2.send(it) // 将在缓冲区被占满时挂起
                }
            }
            // 没有接收到东西……只是等待……
//            repeat(10){
//                channel2.receive()
//            }
            // 没有接收到东西……只是等待……
            delay(1000)
            sender.cancel() // 取消发送者协程


            /**
             * 通道是公平的 遵守先进先出原则
             */
            logLine("通道是公平的")


            /**
             * 每次经过特定的延迟都会从该通道进行消费并产生 Unit
             */
            logLine("计时器通道")
            val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
            var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
            println("Initial element is available immediately: $nextElement") // 初始尚未经过的延迟

            nextElement =
                withTimeoutOrNull(50) { tickerChannel.receive() } // 所有随后到来的元素都经过了 100 毫秒的延迟
            println("Next element is not ready in 50 ms: $nextElement")

            nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
            println("Next element is ready in 100 ms: $nextElement")

            // 模拟大量消费延迟
            println("Consumer pauses for 150ms")
            delay(150)
            // 下一个元素立即可用
            nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
            println("Next element is available immediately after large consumer delay: $nextElement")
            // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
            nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
            println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

            tickerChannel.cancel() // 表明不再需要更多的元素
        }
    }
}