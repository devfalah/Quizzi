package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.utilities.State
import io.reactivex.rxjava3.core.Observable

interface QuizRepository {
    fun getAllQuestions(): Observable<State<QuizResponse>>
}