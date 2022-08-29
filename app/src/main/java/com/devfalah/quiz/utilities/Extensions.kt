package com.devfalah.quiz.utilities

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <E> MutableList<E>.addAll(elements: List<E?>) {
    elements.forEach {
        this.add(it!!)
    }
}

fun <T> Observable<T>.observeOnMainThread() {
    observeOn(AndroidSchedulers.mainThread())
}

fun Disposable.add(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}