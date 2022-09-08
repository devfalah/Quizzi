package com.devfalah.quiz.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment

abstract class BaseDialogFragment<VB : ViewDataBinding> : DialogFragment() {
    abstract val layoutId: Int
    abstract fun setup()

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
        dialog?.setCancelable(false)
        setup()
        return _binding.root
    }
}