/*
 * Copyright (c) 2019 Huami Inc. All Rights Reserved.
 */
package com.example.jetpackdemo.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
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
        val navController = NavHostFragment.findNavController(this)
        navigate_btn.setOnClickListener {
            navController.navigate(R.id.action_BFragment_to_AFragment)
        }
    }
}