package com.devfalah.quiz.ui.mcq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentMcqBinding
import com.devfalah.quiz.ui.base.BaseFragment

class McqFragment:BaseFragment<FragmentMcqBinding>(){
    override val layoutId = R.layout.fragment_mcq
     private val viewModel : McqViewModel by viewModels()
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) ->
    FragmentMcqBinding =DataBindingUtil::inflate
    override fun setup() {
        binding?.apply {
            this.lifecycleOwner = viewLifecycleOwner
            this.viewModel = this@McqFragment.viewModel
        }
    }


}