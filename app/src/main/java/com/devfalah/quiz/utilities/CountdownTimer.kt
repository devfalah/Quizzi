package com.devfalah.quiz.utilities

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


abstract class CountdownTimer(private val endValue: Long, private val timeUnit: TimeUnit) {
    private lateinit var compositeDisposable: CompositeDisposable

    abstract fun onTick(tickValue: Long)

    abstract fun onFinish()

    fun start() {
        compositeDisposable = CompositeDisposable()
        val rangeObservable = Observable.range(0, endValue.toInt())
        val intervalObservable = Observable.interval(1, timeUnit)

        Observable.zip(
            rangeObservable, intervalObservable
        ) { i: Int, l: Long ->
            endValue - i
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(::onNext, ::onError, ::onComplete)
            .add(compositeDisposable)
    }

    private fun onNext(long: Long) {
        onTick(long)
    }

    private fun onError(e: Throwable) {
        e.printStackTrace()
    }

    private fun onComplete() {
        onFinish()
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

}


