package com.devfalah.quiz.utilities

import android.text.Html
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.devfalah.quiz.data.model.Answer
import com.devfalah.quiz.utilities.enums.AnswerState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T> Observable<T>.observeOnMainThread(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Disposable.add(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun String.toMCQAnswer(isCorrect: Boolean, answerState: AnswerState = AnswerState.UNSELECTED) =
    Answer(this, isCorrect, answerState)

fun <E> MutableList<E>.replaceAtIndex(index: Int, newValue: E) {
    this.removeAt(index)
    this.add(index, newValue)

}

fun String.decodeHtml(): String = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()

fun View.goToFragment(navDir: NavDirections) {
    this.findNavController().navigate(navDir)
}

