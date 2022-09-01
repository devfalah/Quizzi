package com.devfalah.quiz.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentHomeBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.utilities.goToFragment

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId = R.layout.fragment_home
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentHomeBinding =
        DataBindingUtil::inflate

    override fun setup() {
        addCallbacks()
    }

    private fun addCallbacks() {
        binding?.let {
            it.playButton.setOnClickListener { view ->
                view.goToFragment(HomeFragmentDirections.actionHomeFragmentToMcqFragment())
            }
            it.howToPlayButton.setOnClickListener { view ->
                view.goToFragment(HomeFragmentDirections.actionHomeFragmentToHowToPlayDialog())
            }
            setOnBackButtonPressedListener()
        }
    }

    private fun setOnBackButtonPressedListener() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}
