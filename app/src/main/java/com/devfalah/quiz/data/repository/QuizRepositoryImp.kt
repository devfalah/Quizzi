package com.devfalah.quiz.data.repository

import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.data.response.QuizResponse
import com.devfalah.quiz.data.service.QuizApiService
import io.reactivex.rxjava3.core.Single


class QuizRepositoryImp(private val quizApiService: QuizApiService) : QuizRepository {
    override fun getQuizQuestions(amount: Int, category: Int?, difficulty: String?, type: String?, ): Single<State<QuizResponse>> {
        return quizApiService.getQuizQuestions(amount, category, difficulty, type).map {
            if (it.isSuccessful) {
                State.Success(it.body())
            } else {
                State.Error(it.message())
            }
        }

    }
}