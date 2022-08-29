package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.utilities.McqDifficulty
import com.devfalah.quiz.utilities.State
import io.reactivex.rxjava3.core.Single

interface QuizRepository {
    fun getQuizQuestions(
        difficulty: McqDifficulty,
    ): Single<State<QuizResponse>>
}