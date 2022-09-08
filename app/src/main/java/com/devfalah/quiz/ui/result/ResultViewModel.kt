package com.devfalah.quiz.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devfalah.quiz.domain.enums.GameState
import com.devfalah.quiz.ui.base.BaseViewModel
import com.devfalah.quiz.utilities.Event
import com.devfalah.quiz.utilities.postEvent

class ResultViewModel : BaseViewModel() {
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _correctAnswersCount = MutableLiveData(0)
    val correctAnswersCount: LiveData<Int> get() = _correctAnswersCount

    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> get() = _gameState

    private val _navigateToHome = MutableLiveData<Event<Boolean>>()
    val navigateToHome : LiveData<Event<Boolean>> = _navigateToHome

    fun setResult(score: Int, correctAnswerCount: Int,gameState: GameState) {
        _score.postValue(score)
        _correctAnswersCount.postValue(correctAnswerCount)
        _gameState.postValue(gameState)
    }

    fun onClickHomeButton() {
        _navigateToHome.postEvent(true)
    }
}