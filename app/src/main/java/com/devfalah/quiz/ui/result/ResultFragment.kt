package com.devfalah.quiz.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentResultBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.ui.mcq.McqViewModel


class ResultFragment : BaseFragment<FragmentResultBinding>() {
    override val layoutId = R.layout.fragment_result
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentResultBinding
        get() = DataBindingUtil::inflate
    private val viewModel : ResultViewModel by viewModels()


    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ResultFragment.viewModel
        }

    }


}