package com.devfalah.quiz.utilities

object Constants {
    const val BASE_URL = "https://opentdb.com/"
    const val MCQ_TYPE = "multiple"
    const val MCQ_AMOUNT = 6

    val SCORE_LIST = listOf(
        100,
        200,
        300,
        500,
        1000,
        2000,
        4000,
        8000,
        16000,
        32000,
        64000,
        125000,
        25000,
        500000,
        1000000,
    )

    const val FOR_REPLACE_EASY_MCQ_INDEX = 0
    const val FOR_REPLACE_MEDIUM_MCQ_INDEX = 1
    const val FOR_REPLACE_HARD_MCQ_INDEX = 2
    const val MCQ_TIMER = 30
    const val MINIMUM_REQUIRED_CORRECT_ANSWERS_TO_PASS = 7
    const val DATA_IS_NULL_ERROR_MESSAGE = "Something went wrong while trying to retrieve data!"
    const val TIMER_ERROR_MESSAGE = "Something went wrong while tyring to handle the timer!"
}