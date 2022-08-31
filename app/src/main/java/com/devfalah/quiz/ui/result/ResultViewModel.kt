package com.devfalah.quiz.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ResultViewModel : ViewModel() {
    private val _score = MutableLiveData<Int>(0)
    val score :LiveData<Int> get() = _score
    private val _correctAnswerCount = MutableLiveData<Int>(0)
    val correctAnswerCount :LiveData<Int> get() = _correctAnswerCount



    fun setResult(score: Int , correctAnswerCount:Int){
        _score.postValue(score)
        _correctAnswerCount.postValue(correctAnswerCount)
    }
}