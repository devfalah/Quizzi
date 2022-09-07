package com.devfalah.quiz.utilities

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

fun doAfterDelay(delay: Long, timeUnit: TimeUnit = TimeUnit.SECONDS, lambda: () -> Unit) {
    Single.timer(delay, timeUnit).observeOn(AndroidSchedulers.mainThread())
        .map { lambda() }.subscribe()
}