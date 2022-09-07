package com.devfalah.quiz.ui.gaming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devfalah.quiz.data.repository.QuizRepositoryImp
import com.devfalah.quiz.data.response.Quiz
import com.devfalah.quiz.data.response.QuizResponse
import com.devfalah.quiz.data.service.WebRequest
import com.devfalah.quiz.domain.enums.AnswerState
import com.devfalah.quiz.domain.enums.QuestionDifficulty
import com.devfalah.quiz.domain.model.Answer
import com.devfalah.quiz.utilities.*
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class GamingViewModel : ViewModel() {
    private val repository = QuizRepositoryImp(WebRequest().apiService)

    private val _requestState = MutableLiveData<State<QuizResponse>>(State.Loading)
    val requestState: LiveData<State<QuizResponse>> get() = _requestState

    private val _currentQuestion = MutableLiveData<Quiz>()
    val currentQuestion: LiveData<Quiz> = _currentQuestion

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex

    private val _currentQuestionAnswers = MutableLiveData<List<Answer>>()
    val currentQuestionAnswers: LiveData<List<Answer>> get() = _currentQuestionAnswers

    private var _correctAnswersCount = MutableLiveData(0)
    val correctAnswersCount: LiveData<Int> get() = _correctAnswersCount

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _isGameOver = MutableLiveData<Event<Boolean>>()
    val isGameOver: LiveData<Event<Boolean>> get() = _isGameOver

    private val _isReplaceQuestionUsed = MutableLiveData(false)
    val isReplaceQuestionUsed: LiveData<Boolean> get() = _isReplaceQuestionUsed

    private val _isDelete2AnswersUsed = MutableLiveData(false)
    val isDelete2AnswersUsed: LiveData<Boolean> get() = _isDelete2AnswersUsed

    private val _time = MutableLiveData(Constants.MCQ_TIMER)
    val time: LiveData<Int> get() = _time

    private lateinit var timer: Observable<Long>
    private lateinit var compositeDisposable: CompositeDisposable

    private val _isQuestionClickable = MutableLiveData(true)
    val isQuestionClickable: LiveData<Boolean> get() = _isQuestionClickable

    private val _error = MutableLiveData<Event<Throwable>>()
    val error: LiveData<Event<Throwable>> get() = _error

    private val _openExitDialog = MutableLiveData<Event<Boolean>>()
    val openExitDialog : LiveData<Event<Boolean>> = _openExitDialog

    init {
        getAllQuestions()
        prepareTimer()
    }

    private fun getAllQuestions() {
        repository.getAllQuestions().run {
            observeOnMainThread()
            subscribe(::onGetQuestionsSuccess, ::onGetQuestionsError)
        }
    }

    private fun onGetQuestionsSuccess(state: State<QuizResponse>) {
        if (state is State.Success) {
            sortQuestionsAccordingToDifficulty(state)
        } else {
            _requestState.postValue(state)
        }
    }

    private fun onGetQuestionsError(throwable: Throwable) {
        _requestState.postValue(State.Error(requireNotNull(throwable.message)))
    }

    private val allMCQsList = mutableListOf<Quiz>()
    private val forReplaceMCQsList = mutableListOf<Quiz>()

    private fun sortQuestionsAccordingToDifficulty(state: State<QuizResponse>) {
        val result = getNotNullList(state.toData()?.questions)
        when (result.first().difficulty) {
            QuestionDifficulty.EASY.name.lowercase() -> sortQuestionsAccordingToPriority(result)
            QuestionDifficulty.MEDIUM.name.lowercase() -> sortQuestionsAccordingToPriority(result)
            QuestionDifficulty.HARD.name.lowercase() -> {
                sortQuestionsAccordingToPriority(result)
                onAllQuestionsSortedSuccessfully(state)
            }
        }
    }

    private fun <T> getNotNullList(list: List<T?>?): List<T> {
        val notNullList = mutableListOf<T>()
        if (list.isNullOrEmpty()) {
            reportError(NullPointerException(Constants.DATA_IS_NULL_ERROR_MESSAGE))
        } else {
            list.forEach {
                if (it == null) {
                    reportError(NullPointerException(Constants.DATA_IS_NULL_ERROR_MESSAGE))
                } else {
                    notNullList.add(it)
                }
            }
        }
        return notNullList
    }

    private fun sortQuestionsAccordingToPriority(questionsList: List<Quiz>) {
        questionsList.subList(0, 5).forEach { allMCQsList.add(it) }
            .also { forReplaceMCQsList.add(questionsList.last()) }
    }

    private fun onAllQuestionsSortedSuccessfully(state: State<QuizResponse>) {
        startTimer()
        _requestState.postValue(state)
        setCurrentQuestion(allMCQsList.first())
    }

    private fun setCurrentQuestion(quiz: Quiz) {
        _currentQuestion.postValue(quiz)
        setCurrentQuestionAnswers(quiz)
    }

    private fun setCurrentQuestionAnswers(quiz: Quiz) {
        if (quiz.correctAnswer != null) {
            val answersList = getNotNullList(quiz.incorrectAnswers)
                .map { it.toAnswer(false) }
                .plus(quiz.correctAnswer.toAnswer(true))
                .shuffled()
            _currentQuestionAnswers.postValue(answersList)
        } else {
            reportError(NullPointerException(Constants.DATA_IS_NULL_ERROR_MESSAGE))
        }
    }

    fun onAnswerClick(answer: Answer) {
        disposeTimer()
        _isQuestionClickable.postValue(false)
        if (answer.isCorrect) {
            onAnswerCorrectly(answer)
        } else {
            onAnswerWrongly(answer)
        }
    }

    private fun onAnswerCorrectly(answer: Answer) {
        _currentQuestionAnswers.postValue(_currentQuestionAnswers.value?.apply {
            answer.state = AnswerState.SELECTED_CORRECT
        })
        _correctAnswersCount.value = _correctAnswersCount.value?.plus(1)
        _score.postValue(_currentQuestionIndex.value?.let { setScore(it) })
        if (isNotLastQuestion()) goToNextQuestion() else endGame()
    }

    private fun setScore(currentMCQIndex: Int): Int {
        return Constants.SCORE_LIST[currentMCQIndex]
    }

    private fun isNotLastQuestion(): Boolean =
        requireNotNull(currentQuestionIndex.value) < allMCQsList.lastIndex

    private fun goToNextQuestion() {
        startTimer()
        _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)
        doAfterDelay {
            _isQuestionClickable.postValue(true)
            _currentQuestionIndex.value?.let { setCurrentQuestion(allMCQsList[it]) }
        }
    }

    private fun onAnswerWrongly(answer: Answer) {
        _currentQuestionAnswers.postValue(_currentQuestionAnswers.value?.apply {
            answer.state = AnswerState.SELECTED_INCORRECT
            endGame()
            this.filter { it.isCorrect }.forEach { it.state = AnswerState.SELECTED_CORRECT }
        })
    }

    private fun endGame() {
        doAfterDelay {
            _isGameOver.postEvent(true)
        }
    }

    fun onReplaceQuestionClickListener() {
        disposeTimer()
        _currentQuestion.value?.let {
            when (it.difficulty) {
                QuestionDifficulty.EASY.name.lowercase() -> replaceQuestion(forReplaceMCQsList[Constants.FOR_REPLACE_EASY_MCQ_INDEX])
                QuestionDifficulty.MEDIUM.name.lowercase() -> replaceQuestion(forReplaceMCQsList[Constants.FOR_REPLACE_MEDIUM_MCQ_INDEX])
                QuestionDifficulty.HARD.name.lowercase() -> replaceQuestion(forReplaceMCQsList[Constants.FOR_REPLACE_HARD_MCQ_INDEX])
            }
        }
    }

    private fun replaceQuestion(newQuestion: Quiz) {
        startTimer()
        showAllAnswersStates()
        _isReplaceQuestionUsed.postValue(true)
        doAfterDelay {
            currentQuestionIndex.value?.let {
                allMCQsList.replaceAtIndex(it, newQuestion)
                setCurrentQuestion(allMCQsList[it])
            }
        }
    }

    fun onDelete2AnswersClickListener() {
        _currentQuestionAnswers.value?.let { listOfAnswers ->
            val permanentIncorrectAnswer = listOfAnswers.filter { !it.isCorrect }.random()
            val newAnswersList = listOfAnswers.onEach { answer ->
                if (!answer.isCorrect && answer != permanentIncorrectAnswer) {
                    answer.isDeleted = true
                }
            }
            _currentQuestionAnswers.postValue(newAnswersList)
            _isDelete2AnswersUsed.postValue(true)
        }
    }

    private fun prepareTimer() {
        val rangeObservable = Observable.range(1, Constants.MCQ_TIMER)
        val intervalObservable = Observable.interval(1, TimeUnit.SECONDS)
        timer = Observable.zip(
            rangeObservable, intervalObservable
        ) { i: Int, _: Long ->
            Constants.MCQ_TIMER.toLong() - i
        }.observeOnMainThread()
    }

    private fun startTimer() {
        compositeDisposable = CompositeDisposable()
        timer.subscribe(::onNext, ::onError, ::onComplete).add(compositeDisposable)
    }

    private fun onNext(count: Long) {
        _time.postValue(count.toInt())
    }

    private fun onError(e: Throwable) {
        e.printStackTrace()
        reportError(Exception(Constants.TIMER_ERROR_MESSAGE))
    }

    private fun onComplete() {
        showAllAnswersStates()
        endGame()
    }

    private fun disposeTimer() {
        compositeDisposable.dispose()
    }

    private fun showAllAnswersStates() {
        _isQuestionClickable.postValue(false)
        _currentQuestionAnswers.postValue(_currentQuestionAnswers.value?.onEach {
            if (it.isCorrect) {
                it.state = AnswerState.TIMEOUT_CORRECT
            } else {
                it.state = AnswerState.TIMEOUT_INCORRECT
            }
        })
    }

    private fun reportError(error: Throwable) {
        _error.postEvent(error)
    }

    private fun doAfterDelay(lambda: () -> Unit) {
        Observable.timer(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
            .map { lambda() }.subscribe()
    }

    fun onClickExitButton() {
        _openExitDialog.postEvent(true)
    }

    fun tryPlayingAgain() {
        _requestState.postValue(State.Loading)
        getAllQuestions()
    }
}