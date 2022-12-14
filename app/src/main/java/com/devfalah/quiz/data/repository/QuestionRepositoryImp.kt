package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.response.QuestionResponse
import com.devfalah.quiz.data.service.QuestionApiService
import com.devfalah.quiz.domain.enums.QuestionDifficulty
import com.devfalah.quiz.data.State
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class QuestionRepositoryImp(private val quizApiService: QuestionApiService) : QuestionRepository {
    override fun getAllQuestions(): Observable<State<QuestionResponse>> {
        return Observable.concat(
            getQuestionsAccordingToDifficulty(QuestionDifficulty.EASY).toObservable(),
            getQuestionsAccordingToDifficulty(QuestionDifficulty.MEDIUM).toObservable(),
            getQuestionsAccordingToDifficulty(QuestionDifficulty.HARD).toObservable(),
        )
    }

    private fun getQuestionsAccordingToDifficulty(difficulty: QuestionDifficulty): Single<State<QuestionResponse>> {
        return wrapResponse(quizApiService.getQuestions(difficulty.name.lowercase()))
    }

    private fun <T> wrapResponse(response: Single<Response<T>>): Single<State<T>> {
        return response.map {
            if (it.isSuccessful) {
                State.Success(it.body())
            } else {
                State.Error(it.message())
            }
        }
    }
}