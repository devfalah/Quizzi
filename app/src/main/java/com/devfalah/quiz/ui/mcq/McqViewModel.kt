package com.devfalah.quiz.ui.mcq

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devfalah.quiz.data.repository.QuizRepositoryImp
import com.devfalah.quiz.data.response.QuizResponse
import com.devfalah.quiz.data.response.Result
import com.devfalah.quiz.data.service.WebRequest
import com.devfalah.quiz.utilities.McqDifficulty
import com.devfalah.quiz.utilities.State
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class McqViewModel : ViewModel() {
    private val repository = QuizRepositoryImp(WebRequest().apiService)

    private var _requestState = MutableLiveData<State<QuizResponse>>()
    val requestState get() = _requestState

    val easyQuestions = mutableListOf<Result>()
    val mediumQuestions = mutableListOf<Result>()
    val hardQuestions = mutableListOf<Result>()

    init {
        getFifteenMCQs()
    }

    private fun getFifteenMCQs() {
        Observable.concat(getFiveMCQs(McqDifficulty.EASY).toObservable(),
            getFiveMCQs(McqDifficulty.MEDIUM).toObservable(),
            getFiveMCQs(McqDifficulty.HARD).toObservable()
        ).run {
            subscribeOn(Schedulers.io())
            observeOn(AndroidSchedulers.mainThread())
            subscribe(::onGetMCQsSuccess, ::onGetMCQsError)
        }
    }

    private fun onGetMCQsSuccess(state: State<QuizResponse>) {
        sortMCQsDueToDifficulty(state)
    }

    private fun sortMCQsDueToDifficulty(state: State<QuizResponse>) {
        val results = requireNotNull(state.toData()?.results)
        when (results[0]?.difficulty) {
            McqDifficulty.EASY.name.lowercase() -> results.forEach { easyQuestions.add(it!!) }
            McqDifficulty.MEDIUM.name.lowercase() -> results.forEach { mediumQuestions.add(it!!) }
            McqDifficulty.HARD.name.lowercase() -> {
                results.forEach { hardQuestions.add(it!!) }
                if (state is State.Success) _requestState.postValue(state)
            }
        }
    }

    private fun onGetMCQsError(throwable: Throwable) = _requestState.postValue(State.Error(requireNotNull(throwable.message)))

    private fun getFiveMCQs(difficulty: McqDifficulty): Single<State<QuizResponse>> = repository.getQuizQuestions(difficulty)
}