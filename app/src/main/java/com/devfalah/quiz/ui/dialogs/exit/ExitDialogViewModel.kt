package com.devfalah.quiz.ui.dialogs.exit

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devfalah.quiz.ui.base.BaseViewModel
import com.devfalah.quiz.utilities.Event
import com.devfalah.quiz.utilities.postEvent

class ExitDialogViewModel : BaseViewModel() {

    private val _closeDialog = MutableLiveData<Event<Boolean>>()
    val closeDialog : LiveData<Event<Boolean>> = _closeDialog

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome : LiveData<Event<Boolean>> = _navigateToHome


    fun onClickCloseIcon() {
        _closeDialog.postEvent(true)
    }


    fun onClickExitButton() {
        _navigateToHome.postEvent(true)
    }
}