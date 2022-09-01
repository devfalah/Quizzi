package com.devfalah.quiz.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score
    private val _correctAnswersCount = MutableLiveData(0)
    val correctAnswersCount: LiveData<Int> get() = _correctAnswersCount

    fun setResult(score: Int, correctAnswerCount: Int) {
        _score.postValue(score)
        _correctAnswersCount.postValue(correctAnswerCount)
    }
}