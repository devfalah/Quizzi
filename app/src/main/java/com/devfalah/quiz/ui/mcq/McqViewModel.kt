package com.devfalah.quiz.ui.mcq

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devfalah.quiz.data.model.Answer
import com.devfalah.quiz.data.model.Quiz
import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.data.repository.QuizRepositoryImp
import com.devfalah.quiz.data.service.WebRequest
import com.devfalah.quiz.utilities.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class McqViewModel : ViewModel() {
    private val repository = QuizRepositoryImp(WebRequest().apiService)

    private var _requestState = MutableLiveData<State<QuizResponse>>(State.Loading)
    val requestState: LiveData<State<QuizResponse>> get() = _requestState

    private val _currentMCQ = MutableLiveData<Quiz>()
    val currentMCQ: LiveData<Quiz> get() = _currentMCQ

    private val _currentMCQAnswers = MutableLiveData<List<Answer>>()
    val currentMCQAnswers: LiveData<List<Answer>> get() = _currentMCQAnswers

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _currentMCQIndex = MutableLiveData(0)
    val currentMCQIndex: LiveData<Int> get() = _currentMCQIndex

    private val _isReplaceMCQUsed = MutableLiveData(false)
    val isReplaceMCQUsed: LiveData<Boolean> get() = _isReplaceMCQUsed

    init {
        getAllMCQs()
    }

    private fun getAllMCQs() {
        Observable.concat(
            getMCQs(McqDifficulty.EASY).toObservable(),
            getMCQs(McqDifficulty.MEDIUM).toObservable(),
            getMCQs(McqDifficulty.HARD).toObservable()
        ).run {
            observeOnMainThread()
            subscribe(::onGetMCQsSuccess, ::onGetMCQsError)
        }
    }

    private fun getMCQs(difficulty: McqDifficulty): Single<State<QuizResponse>> = repository.getQuizQuestions(difficulty)

    private fun onGetMCQsSuccess(state: State<QuizResponse>) = if (state is State.Success) sortMCQsAccordingToDifficulty(state) else _requestState.postValue(state)

    private fun onGetMCQsError(throwable: Throwable) = _requestState.postValue(State.Error(requireNotNull(throwable.message)))

    private val allMCQsList = mutableListOf<Quiz>()
    private val forReplaceMCQsList = mutableListOf<Quiz>()

    private fun sortMCQsAccordingToDifficulty(state: State<QuizResponse>) {
        val result = requireNotNull(state.toData()?.questions)
        when (result.first()?.difficulty) {
            McqDifficulty.EASY.name.lowercase() -> sortMCQsAccordingToPriority(result)
            McqDifficulty.MEDIUM.name.lowercase() -> sortMCQsAccordingToPriority(result)
            McqDifficulty.HARD.name.lowercase() -> {
                sortMCQsAccordingToPriority(result)
                onAllMCQsSortedSuccessfully(state)
            }
        }
    }

    private fun sortMCQsAccordingToPriority(mcqList: List<Quiz?>) = mcqList.subList(0, 5).forEach { allMCQsList.add(it!!) }.also { forReplaceMCQsList.add(mcqList.last()!!) }

    private fun onAllMCQsSortedSuccessfully(state: State<QuizResponse>) {
        _requestState.postValue(state)
        setCurrentMCQ(allMCQsList.first())
    }

    private fun setCurrentMCQ(quiz: Quiz) {
        _currentMCQ.postValue(quiz)
        setCurrentMCQAnswers(quiz)
    }

    private fun setCurrentMCQAnswers(quiz: Quiz) {
        val listOfAnswers = quiz.incorrectAnswers!!.map { it!!.toMCQAnswer(false) }.plus(quiz.correctAnswer!!.toMCQAnswer(true)).shuffled()
        _currentMCQAnswers.postValue(listOfAnswers)
    }

    fun onClickAnswer(answer: Answer) {
        if (answer.isCorrect) _score.postValue(_score.value?.plus(Constants.SCORE))
        goToNextMCQ()
    }

    private fun goToNextMCQ() {
        if (currentMCQIndex.value!! < allMCQsList.lastIndex) {
            _currentMCQIndex.value = _currentMCQIndex.value!! + 1
            setCurrentMCQ(allMCQsList[_currentMCQIndex.value!!])
        } else endGame()
    }

    private fun endGame() {
        // Do something here
    }

    fun onReplaceMCQClickListener() {
        when (currentMCQ.value!!.difficulty) {
            McqDifficulty.EASY.name.lowercase() -> replaceMCQ(forReplaceMCQsList[Constants.FOR_REPLACE_EASY_MCQ_INDEX])
            McqDifficulty.MEDIUM.name.lowercase() -> replaceMCQ(forReplaceMCQsList[Constants.FOR_REPLACE_MEDIUM_MCQ_INDEX])
            McqDifficulty.HARD.name.lowercase() -> replaceMCQ(forReplaceMCQsList[Constants.FOR_REPLACE_HARD_MCQ_INDEX])
        }
    }

    private fun replaceMCQ(newMCQ: Quiz) {
        allMCQsList.replaceAtIndex(currentMCQIndex.value!!, newMCQ)
        setCurrentMCQ(allMCQsList[currentMCQIndex.value!!])
        _isReplaceMCQUsed.postValue(true)
    }

}