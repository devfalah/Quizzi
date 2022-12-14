package com.devfalah.quiz.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devfalah.quiz.ui.base.BaseViewModel
import com.devfalah.quiz.utilities.Event
import com.devfalah.quiz.utilities.postEvent


class HomeViewModel : BaseViewModel() {
    private val _navigateToMCQ = MutableLiveData<Event<Boolean>>()
    val navigateToMCQ : LiveData<Event<Boolean>> = _navigateToMCQ
    private val _openHowToPlayDialog = MutableLiveData<Event<Boolean>>()
    val openHowToPlayDialog : LiveData<Event<Boolean>> = _openHowToPlayDialog


    fun onClickPlayButton() {
        _navigateToMCQ.postEvent(true)
    }
    fun onClickHowPlayButton() {
        _openHowToPlayDialog.postEvent(true)
    }


}