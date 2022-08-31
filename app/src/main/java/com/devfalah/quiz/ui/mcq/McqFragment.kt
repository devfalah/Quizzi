package com.devfalah.quiz.ui.mcq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentMcqBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.ui.home.HomeFragmentDirections

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

        binding?.exitIcon?.setOnClickListener{ v->
            val action = McqFragmentDirections.actionMcqFragmentToExitDialog()
            v.findNavController().navigate(action)
        }

        viewModel.correctAnswersCount.observe(this){
            val action  = McqFragmentDirections.actionMcqFragmentToResultFragment(this.viewModel.correctAnswersCount.value!! , this.viewModel.score.value!!)
            requireView().findNavController().navigate(action)
        }

    }
}