package com.example.jetpackdemo.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.jetpackdemo.R
import kotlinx.android.synthetic.main.a_fragment.*

class AFragment : Fragment() {

    val args: AFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.a_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(context, args.text, Toast.LENGTH_SHORT).show()
        navigate_btn.setOnClickListener {
            // 不建议
            // val navController = findNavController(this)
            // navController.navigate(R.id.action_AFragment_to_BFragment)
            val action = AFragmentDirections.actionAFragmentToBFragment()
            it.findNavController().navigate(action)
        }
    }
}