package com.devfalah.quiz.data.response

import com.google.gson.annotations.SerializedName

data class QuizResponse(
    @SerializedName("response_code")
    val responseCode: Int?,
    @SerializedName("results")
    val questions: List<Quiz?>?
)