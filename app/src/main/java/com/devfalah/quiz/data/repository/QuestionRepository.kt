package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.response.QuestionResponse
import com.devfalah.quiz.utilities.State
import io.reactivex.rxjava3.core.Observable

interface QuestionRepository {
    fun getAllQuestions(): Observable<State<QuestionResponse>>
}