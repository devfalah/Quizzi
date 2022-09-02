package com.devfalah.quiz.ui.mcq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentMcqBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.goToFragment

class McqFragment : BaseFragment<FragmentMcqBinding>() {
    override val layoutId = R.layout.fragment_mcq
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentMcqBinding = DataBindingUtil::inflate
    private val viewModel: McqViewModel by viewModels()

    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@McqFragment.viewModel
        }
        addCallbacks()
        setGameOverObserver()
    }

    private fun addCallbacks() {
        setOnBackButtonClickListener()
        setOnTryAgainButtonClickListener()
        setOnExitIconClickListener()
    }

    private fun setOnBackButtonClickListener() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.requestState.value is State.Success) showExitDialog()
                else findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setOnTryAgainButtonClickListener() {
        binding!!.error.tryAgain.setOnClickListener {
            viewModel.tryPlayingAgain()
        }
    }
    private fun setOnExitIconClickListener() {
        binding!!.exitIcon.setOnClickListener { view ->
            showExitDialog()
        }
    }
    private fun showExitDialog(){
        requireView().goToFragment(McqFragmentDirections.actionMcqFragmentToExitDialog())
    }

    private fun setGameOverObserver() {
        viewModel.isGameOver.observe(this) {
            val action = McqFragmentDirections.actionMcqFragmentToResultFragment(
                this.viewModel.correctAnswersCount.value!!,
                this.viewModel.score.value!!
            )
            requireView().goToFragment(action)
        }
    }
}