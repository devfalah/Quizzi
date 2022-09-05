package com.devfalah.quiz.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentHomeBinding
import com.devfalah.quiz.ui.base.BaseFragment
import com.devfalah.quiz.utilities.observeEvent

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId = R.layout.fragment_home
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentHomeBinding =
        DataBindingUtil::inflate
    private val viewModel: HomeViewModel by viewModels()

    override fun setup() {
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@HomeFragment.viewModel
        }
        handleObserveEvents()
        setOnBackButtonClickListener()
    }

    private fun handleObserveEvents() {
        viewModel.apply {
            navigateToMCQ.observeEvent(this@HomeFragment) {
                view?.findNavController()
                    ?.navigate(HomeFragmentDirections.actionHomeFragmentToGamingFragment())
            }
            openHowToPlayDialog.observeEvent(this@HomeFragment) {
                view?.findNavController()
                    ?.navigate(HomeFragmentDirections.actionHomeFragmentToHowToPlayDialog())
            }
        }
    }

    private fun setOnBackButtonClickListener() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

}
