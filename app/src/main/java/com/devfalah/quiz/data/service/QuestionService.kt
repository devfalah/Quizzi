package com.devfalah.quiz.data.service

import com.devfalah.quiz.data.response.QuestionResponse
import com.devfalah.quiz.utilities.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionApiService {
    @GET("api.php")
    fun getQuizQuestions(
        @Query("difficulty") difficulty: String?,
        @Query("amount") amount: Int = Constants.MCQ_AMOUNT,
        @Query("type") type: String? = Constants.MCQ_TYPE,
        @Query("category") category: Int? = null,
    ): Single<Response<QuestionResponse>>
}