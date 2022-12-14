package com.devfalah.quiz.utilities

import android.text.Html
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devfalah.quiz.domain.model.Answer
import com.devfalah.quiz.domain.enums.AnswerState
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers

fun <T : Any> Observable<T>.observeOnMainThread(): Observable<T> {
    return subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}

fun Disposable.add(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun String.toAnswer(isCorrect: Boolean, answerState: AnswerState = AnswerState.UNSELECTED,isDeleted : Boolean = false) =
    Answer(this, isCorrect, answerState, isDeleted)

fun <E> MutableList<E>.replaceAtIndex(index: Int, newValue: E) {
    this.removeAt(index)
    this.add(index, newValue)

}

fun String.decodeHtml(): String = Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT).toString()


fun <T> MutableLiveData<Event<T>>.postEvent(content: T) {
    postValue(Event(content))
}

inline fun <T> LiveData<Event<T>>.observeEvent(owner: LifecycleOwner, crossinline onEventUnhandledContent: (T) -> Unit) {
    observe(owner) { it?.getContentIfNotHandled()?.let(onEventUnhandledContent) }
}