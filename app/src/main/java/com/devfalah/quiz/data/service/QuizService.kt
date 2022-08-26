package com.devfalah.quiz.data.service

import com.devfalah.quiz.data.response.QuizResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuizApiService {

    @GET("api.php")
    fun getQuizQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String?,
    ): Single<Response<QuizResponse>>
}