package com.devfalah.quiz.data.repository

import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.data.service.QuizApiService
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.enums.QuestionDifficulty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Response

class QuizRepositoryImp(private val quizApiService: QuizApiService) : QuizRepository {
    override fun getAllQuestions(): Observable<State<QuizResponse>> {
        return Observable.concat(
            getQuestionsAccordingToDifficulty(QuestionDifficulty.EASY).toObservable(),
            getQuestionsAccordingToDifficulty(QuestionDifficulty.MEDIUM).toObservable(),
            getQuestionsAccordingToDifficulty(QuestionDifficulty.HARD).toObservable(),
        )
    }

    private fun getQuestionsAccordingToDifficulty(difficulty: QuestionDifficulty): Single<State<QuizResponse>> {
        return wrapResponse(quizApiService.getQuizQuestions(difficulty.name.lowercase()))
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