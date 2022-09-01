package com.devfalah.quiz.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    abstract val layoutId: Int
    abstract fun setup()

    private lateinit var _binding: ViewBinding
    var binding: VB? = null
        get() = _binding as VB?


    abstract val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = bindingInflater(inflater, layoutId, container, false)
        setup()
        return _binding.root
    }
}



