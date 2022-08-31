package com.devfalah.quiz.ui.mcq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentMcqBinding
import com.devfalah.quiz.ui.base.BaseFragment

class McqFragment:BaseFragment<FragmentMcqBinding>(){
    override val layoutId = R.layout.fragment_mcq
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentMcqBinding =DataBindingUtil::inflate
    private val viewModel : McqViewModel by viewModels()

    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@McqFragment.viewModel
        }
        viewModel.timer.start()

        binding?.exitIcon?.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_mcqFragment_to_exit_dialog)
        }

        viewModel.isGameOver.observe(this) { isGameOver ->
            if (isGameOver) requireView().findNavController().navigate(R.id.action_mcqFragment_to_resultFragment)
        }
    }
}