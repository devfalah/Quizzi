package com.devfalah.quiz.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentHomeBinding
import com.devfalah.quiz.ui.base.BaseFragment


class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val layoutId = R.layout.fragment_home
    override val bindingInflater: (LayoutInflater, Int, ViewGroup?, Boolean) -> FragmentHomeBinding =
        DataBindingUtil::inflate

    override fun setup() {

        addCallbacks()
    }


    private fun addCallbacks() {
        binding?.let {
            it.playButton.setOnClickListener { v ->
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_mcqFragment)
            }
            it.howToPlayButton.setOnClickListener { v ->
                Navigation.findNavController(v)
                    .navigate(R.id.action_homeFragment_to_how_to_play_dialog)
            }
        }

    }

}
