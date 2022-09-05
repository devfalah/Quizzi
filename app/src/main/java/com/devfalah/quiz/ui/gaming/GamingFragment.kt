package com.devfalah.quiz.ui.gaming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentGamingBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.observeEvent

class GamingFragment : BaseFragment<FragmentGamingBinding>() {
    override val layoutId = R.layout.fragment_gaming
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentGamingBinding = DataBindingUtil::inflate
    private val viewModel: GamingViewModel by viewModels()

    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@GamingFragment.viewModel
        }
        addCallbacks()
    }

    private fun addCallbacks() {
        setOnBackButtonClickListener()
        setOnTryAgainButtonClickListener()
        setOnExitIconClickListener()
        handleGameOverObserverEvent()
        handleErrorObserverEvent()
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

    private fun setOnTryAgainButtonClickListener() {
        binding?.let {
            it.error.tryAgain.setOnClickListener {
                viewModel.tryPlayingAgain()
            }
        }
    }

    private fun setOnExitIconClickListener() {
        binding?.let {
            it.exitIcon.setOnClickListener {
                showExitDialog()
            }
        }
    }

    private fun showExitDialog() {
        requireView().findNavController().navigate(GamingFragmentDirections.actionGamingFragmentToExitDialog())
    }

    private fun handleGameOverObserverEvent() {
        viewModel.isGameOver.observeEvent(this) {
            val action = GamingFragmentDirections.actionGamingFragmentToResultFragment(
                this.viewModel.correctAnswersCount.value!!,
                this.viewModel.score.value!!
            )
            requireView().findNavController().navigate(action)
        }
    }

    private fun handleErrorObserverEvent() {
        viewModel.error.observeEvent(this) {
            showErrorAlertDialog(it.message.toString())
        }
    }

    private fun showErrorAlertDialog(errorMessage: String) {
        AlertDialog.Builder(requireContext()).run {
            setTitle("Error")
            setMessage(errorMessage)
            setCancelable(false)
            setPositiveButton("Retry") { _, _ ->
                viewModel.tryPlayingAgain()
            }
            setNegativeButton("Exit") { _, _ ->
                requireView().findNavController().navigate(GamingFragmentDirections.actionGamingFragmentToHomeFragment())
            }
        }.create().show()
    }
}