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
import com.devfalah.quiz.utilities.Constants
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
        handleObserveEvents()
        setOnBackButtonClickListener()
    }

    private fun handleObserveEvents() {
        viewModel.apply {
            isGameOver.observeEvent(this@GamingFragment) {
                val action = GamingFragmentDirections.actionGamingFragmentToResultFragment(
                    viewModel.correctAnswersCount.value!!, viewModel.score.value!!
                )
                requireView().findNavController().navigate(action)
            }
            openExitDialog.observeEvent(this@GamingFragment){
                showExitDialog()
            }
            error.observeEvent(this@GamingFragment) {
                showErrorAlertDialog(it.message.toString())
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
        requireView().findNavController().navigate(GamingFragmentDirections.actionGamingFragmentToExitDialog())
    }

    private fun showErrorAlertDialog(errorMessage: String) {
        AlertDialog.Builder(requireContext()).run {
            setTitle(Constants.ALERT_DIALOG_TILE)
            setMessage(errorMessage)
            setCancelable(false)
            setPositiveButton(Constants.RETRY_BUTTON_TEXT) { _, _ ->
                viewModel.tryPlayingAgain()
            }
            setNegativeButton(Constants.EXIT_BUTTON_TEXT) { _, _ ->
                requireView().findNavController().popBackStack()
            }
        }.create().show()
    }
}