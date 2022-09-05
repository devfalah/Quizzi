package com.devfalah.quiz.ui.dialogs.howToPlay

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devfalah.quiz.R

import com.devfalah.quiz.databinding.FragmentDialogHowToPlayBinding
import com.devfalah.quiz.ui.base.BaseDialogFragment
import com.devfalah.quiz.utilities.observeEvent

class HowToPlayDialogFragment : BaseDialogFragment<FragmentDialogHowToPlayBinding>() {
    override val layoutId: Int = R.layout.fragment_dialog_how_to_play
    override fun bindingInflater(): FragmentDialogHowToPlayBinding = FragmentDialogHowToPlayBinding.inflate(layoutInflater)
    val viewModel: HowToPlayDialogViewModel by viewModels()
    override fun setup() {
        binding.apply {
            lifecycleOwner = this@HowToPlayDialogFragment
            this.viewModel = this@HowToPlayDialogFragment.viewModel
        }
        handleObserveEvents()
    }



    private fun handleObserveEvents() {
        viewModel.apply {
            closeDialog.observeEvent(this@HowToPlayDialogFragment) {
                this@HowToPlayDialogFragment.findNavController().popBackStack()
            }
        }
    }
}