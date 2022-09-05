package com.devfalah.quiz.ui.dialogs.howToPlay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devfalah.quiz.utilities.Event
import com.devfalah.quiz.utilities.postEvent

class HowToPlayDialogViewModel :ViewModel() {

    private val _closeDialog = MutableLiveData<Event<Boolean>>()
    val closeDialog : LiveData<Event<Boolean>> = _closeDialog

    fun onClickCloseIcon() {
        _closeDialog.postEvent(true)
    }
}