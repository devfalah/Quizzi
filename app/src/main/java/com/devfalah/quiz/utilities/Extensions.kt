package com.devfalah.quiz.utilities

import android.text.Html
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.devfalah.quiz.data.model.Answer
import com.devfalah.quiz.utilities.enums.AnswerState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

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

fun String.toMCQAnswer(isCorrect: Boolean,answerState: AnswerState=AnswerState.UNSELECTED) = Answer(this, isCorrect,answerState)

fun <E> MutableList<E>.replaceAtIndex(index: Int, newValue: E) {
    this.removeAt(index)
    this.add(index, newValue)

}

fun String.decodeHtml(): String = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()

fun View.goToFragment(navDir: NavDirections) {
    Navigation.findNavController(this).navigate(navDir)
}