package com.example.jetpackdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.jetpackdemo.build.Msg.Companion.MSG
import com.example.jetpackdemo.channel.ChannelActivity
import com.example.jetpackdemo.coroutines.CoroutinesActivity
import com.example.jetpackdemo.koin.TestActivity
import com.example.jetpackdemo.livedata.LiveDataActivity
import com.example.jetpackdemo.navigation.HostActivity
import com.example.jetpackdemo.paged.PagedActivity
import com.example.jetpackdemo.room.RoomActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, MSG, Toast.LENGTH_SHORT).show()
        supportFragmentManager.also {
            it.beginTransaction()
                .replace(R.id.fv_main, TestFragment.newInstance(Color.RED))
                .commit()
            GlobalScope.launch(Dispatchers.Main) {
                delay(3000)
                it.beginTransaction()
                    .replace(R.id.fv_main, TestFragment.newInstance(Color.BLUE))
                    .commit()
            }
        }
    }

    fun paged(view: View) {
        startActivity(Intent(this, PagedActivity::class.java))
    }

    fun room(view: View) {
        startActivity(Intent(this, RoomActivity::class.java))
    }

    fun koin(view: View) {
        startActivity(Intent(this, TestActivity::class.java))
    }

    fun liveData(view: View) {
        startActivity(Intent(this, LiveDataActivity::class.java))
    }

    fun navigation(view: View) {
        startActivity(Intent(this, HostActivity::class.java))
    }

    fun launch(view: View) {
        startActivity(Intent(this, CoroutinesActivity::class.java))
    }

    fun channel(view: View) {
        startActivity(Intent(this, ChannelActivity::class.java))
    }
}

class TestFragment : Fragment() {

    companion object {
        private const val TAG: String = "TestFragment"
        private const val BG: String = "bg"

        @JvmStatic
        fun newInstance(bg: Int): TestFragment {
            val fragment = TestFragment()
            val bundle = Bundle()
            bundle.putInt(BG, bg)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return TextView(context).run {
            text = TAG
            gravity = Gravity.CENTER
            setBackgroundColor(arguments?.getInt(BG, Color.RED)!!)
            this
        }
    }

}

