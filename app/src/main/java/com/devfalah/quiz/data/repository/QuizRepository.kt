package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.enums.McqDifficulty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface QuizRepository {
    fun getAllQuestions(): Observable<State<QuizResponse>>
}