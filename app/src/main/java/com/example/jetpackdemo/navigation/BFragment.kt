package com.example.jetpackdemo.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.jetpackdemo.R
import kotlinx.android.synthetic.main.a_fragment.*

class BFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.b_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigate_btn.setOnClickListener {
            val action = AFragmentDirections.actionAFragmentToBFragment()
            val bundle = Bundle()
            bundle.putString("text", System.currentTimeMillis().toString())
            it.findNavController().navigate(R.id.action_BFragment_to_AFragment, bundle)
        }
    }
}