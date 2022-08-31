package com.devfalah.quiz.data.model

import com.devfalah.quiz.utilities.AnswerState


data class Answer(
    val answer: String,
    val isCorrect: Boolean,
    var state: AnswerState = AnswerState.UNSELECTED
)