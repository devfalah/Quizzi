package com.devfalah.quiz.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentResultBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.utilities.goToFragment


class ResultFragment : BaseFragment<FragmentResultBinding>() {
    private val args: ResultFragmentArgs by navArgs()
    override val layoutId = R.layout.fragment_result
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentResultBinding
        get() = DataBindingUtil::inflate
    private val viewModel: ResultViewModel by viewModels()


    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@ResultFragment.viewModel
        }
        viewModel.setResult(args.score, args.correctAnswersCount)
        setOnHomeButtonClickListener()

    }




    private fun setOnHomeButtonClickListener(){
        binding!!.homeButton.setOnClickListener { view ->
            view.goToFragment(ResultFragmentDirections.actionResultFragmentToHomeFragment())
        }
    }
}