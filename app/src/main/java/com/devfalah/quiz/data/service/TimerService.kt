package com.devfalah.quiz.data.service

import com.devfalah.quiz.utilities.add
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit


abstract class TimerService(private val endValue: Long, private val timeUnit: TimeUnit) {
    lateinit var compositeDisposable: CompositeDisposable

    abstract fun onTick(tickValue: Long)

    abstract fun onFinish()

    fun start() {
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


