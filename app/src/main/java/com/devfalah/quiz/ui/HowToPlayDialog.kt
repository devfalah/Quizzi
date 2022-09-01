package com.devfalah.quiz.ui

import com.devfalah.quiz.R

import com.devfalah.quiz.databinding.FragmentDialogHowToPlayBinding
import com.devfalah.quiz.ui.base.BaseDialogFragment

class HowToPlayDialogFragment : BaseDialogFragment<FragmentDialogHowToPlayBinding>() {
    override val layoutId: Int = R.layout.fragment_dialog_how_to_play
    override fun bindingInflater(): FragmentDialogHowToPlayBinding = FragmentDialogHowToPlayBinding.inflate(layoutInflater)
    override fun setCloseButton() = binding.closeIcon
}