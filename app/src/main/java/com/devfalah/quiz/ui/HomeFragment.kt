package com.devfalah.quiz.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.devfalah.quiz.R
import com.devfalah.quiz.databinding.FragmentHomeBinding


class HomeFragment : Fragment(R.layout.fragment_home) {
lateinit var binding : FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    binding.playButton.setOnClickListener { v ->

        Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_mcqFragment)

    }

    }


}