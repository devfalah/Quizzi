package com.devfalah.quiz.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding


abstract class BaseDialogFragment<VB : ViewBinding> : DialogFragment() {
    abstract val layoutId: Int

    abstract fun setCloseButton(): View
    private lateinit var closeButton: View

    abstract fun bindingInflater(): VB
    private lateinit var _binding: VB
    val binding: VB
        get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater()
        closeButton = setCloseButton()
        hideDialog()
        return _binding.root
    }

    private fun hideDialog() {
        closeButton.setOnClickListener{
            this.findNavController().popBackStack()
        }
    }


}