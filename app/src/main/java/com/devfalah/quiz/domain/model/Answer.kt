package com.devfalah.quiz.domain.model

import com.devfalah.quiz.domain.enums.AnswerState


data class Answer(
    val answer: String,
    val isCorrect: Boolean,
    var state: AnswerState,
    var hidden : Boolean = false
)