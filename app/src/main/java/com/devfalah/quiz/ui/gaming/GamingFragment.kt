package com.devfalah.quiz.ui.gaming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentGamingBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.data.State
import com.devfalah.quiz.utilities.observeEvent

class GamingFragment : BaseFragment<FragmentGamingBinding>() {
    override val layoutId = R.layout.fragment_gaming
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentGamingBinding =
        DataBindingUtil::inflate
    private val viewModel: GamingViewModel by viewModels()


    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@GamingFragment.viewModel
        }
        handleObserveEvents()
        setOnBackButtonClickListener()
    }

    private fun handleObserveEvents() {
        viewModel.apply {
            isGameOver.observeEvent(this@GamingFragment) {
                val action =
                    GamingFragmentDirections.actionGamingFragmentToResultFragment(viewModel.correctAnswersCount.value
                        ?: 0, viewModel.score.value ?: 0, it)
                requireView().findNavController().navigate(action)
            }
            openExitDialog.observeEvent(this@GamingFragment) {
                showExitDialog()
            }
        }
    }

    private fun setOnBackButtonClickListener() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.requestState.value is State.Success) {
                    showExitDialog()
                } else {
                    findNavController().popBackStack()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun showExitDialog() {
        requireView().findNavController()
            .navigate(GamingFragmentDirections.actionGamingFragmentToExitDialog())
    }


}