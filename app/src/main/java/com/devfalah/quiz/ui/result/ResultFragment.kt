package com.devfalah.quiz.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentResultBinding
import com.devfalah.quiz.ui.base.BaseFragment


class ResultFragment : BaseFragment<FragmentResultBinding>() {
    override val layoutId = R.layout.fragment_result


    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentResultBinding
        get() = DataBindingUtil::inflate

    override fun setup() {

    }


}