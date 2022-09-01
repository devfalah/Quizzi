package com.devfalah.quiz.ui

import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentDialogExitBinding
import com.devfalah.quiz.ui.base.BaseDialogFragment

class ExitDialogFragment : BaseDialogFragment<FragmentDialogExitBinding>() {
    override val layoutId = R.layout.fragment_dialog_exit
    override fun bindingInflater() = FragmentDialogExitBinding.inflate(layoutInflater)
    override fun setCloseButton() = binding.closeIcon
}