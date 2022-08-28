package com.devfalah.quiz.ui.mcq

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.devfalah.quiz.R
import com.devfalah.quiz.utilities.State

class McqFragment:Fragment(R.layout.fragment_mcq) {
    private lateinit var viewModel: McqViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(requireActivity())[McqViewModel::class.java]

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}