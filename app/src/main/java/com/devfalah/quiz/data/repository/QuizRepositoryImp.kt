package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.data.service.QuizApiService
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.enums.McqDifficulty
import io.reactivex.rxjava3.core.Single

class QuizRepositoryImp(private val quizApiService: QuizApiService) : QuizRepository {
    override fun getQuizQuestions(
        difficulty: McqDifficulty,
    ): Single<State<QuizResponse>> {
        return quizApiService.getQuizQuestions(difficulty.name.lowercase()).map {
            if (it.isSuccessful) State.Success(it.body()) else State.Error(it.message())
        }
    }
}