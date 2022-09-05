package com.devfalah.quiz.ui.mcq

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentMcqBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.observeEvent

class McqFragment : BaseFragment<FragmentMcqBinding>() {
    override val layoutId = R.layout.fragment_mcq
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentMcqBinding =
        DataBindingUtil::inflate
    private val viewModel: McqViewModel by viewModels()

    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@McqFragment.viewModel
        }
        addCallbacks()
        handleObserveEvents()
    }

    private fun addCallbacks() {
        setOnBackButtonClickListener()
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



    private fun showExitDialog() {
        view?.findNavController()?.navigate(McqFragmentDirections.actionMcqFragmentToExitDialog())
    }

    private fun handleObserveEvents() {
        viewModel.apply {
            isGameOver.observeEvent(this@McqFragment) {
                val action = McqFragmentDirections.actionMcqFragmentToResultFragment(
                    viewModel.correctAnswersCount.value!!, viewModel.score.value!!
                )
                view?.findNavController()?.navigate(action)
            }
            openExitDialog.observeEvent(this@McqFragment){
                showExitDialog()
            }
        }
    }


}