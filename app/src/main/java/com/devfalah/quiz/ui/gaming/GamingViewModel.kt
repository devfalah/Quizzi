package com.devfalah.quiz.ui.gaming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devfalah.quiz.data.State
import com.devfalah.quiz.data.repository.QuestionRepositoryImp
import com.devfalah.quiz.data.response.Question
import com.devfalah.quiz.data.response.QuestionResponse
import com.devfalah.quiz.data.service.WebRequest
import com.devfalah.quiz.domain.enums.AnswerState
import com.devfalah.quiz.domain.enums.GameState
import com.devfalah.quiz.domain.enums.QuestionDifficulty
import com.devfalah.quiz.domain.model.Answer
import com.devfalah.quiz.ui.base.BaseViewModel
import com.devfalah.quiz.utilities.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class GamingViewModel : BaseViewModel() {
    private val repository = QuestionRepositoryImp(WebRequest().apiService)

    private val _requestState = MutableLiveData<State<QuestionResponse>>(State.Loading)
    val requestState: LiveData<State<QuestionResponse>> get() = _requestState

    private val _currentQuestion = MutableLiveData<Question>()
    val currentQuestion: LiveData<Question> = _currentQuestion

    private val _currentQuestionIndex = MutableLiveData(0)
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex

    private val _currentQuestionAnswers = MutableLiveData<List<Answer>>()
    val currentQuestionAnswers: LiveData<List<Answer>> get() = _currentQuestionAnswers

    private var _correctAnswersCount = MutableLiveData(0)
    val correctAnswersCount: LiveData<Int> get() = _correctAnswersCount

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _isGameOver = MutableLiveData<Event<GameState>>()
    val isGameOver: LiveData<Event<GameState>> get() = _isGameOver

    private val _isReplaceQuestionUsed = MutableLiveData(false)
    val isReplaceQuestionUsed: LiveData<Boolean> get() = _isReplaceQuestionUsed

    private val _isDelete2AnswersUsed = MutableLiveData(false)
    val isDelete2AnswersUsed: LiveData<Boolean> get() = _isDelete2AnswersUsed

    private val _isQuestionClickable = MutableLiveData(true)
    val isQuestionClickable: LiveData<Boolean> get() = _isQuestionClickable


    private val _openExitDialog = MutableLiveData<Event<Boolean>>()
    val openExitDialog : LiveData<Event<Boolean>> = _openExitDialog

    private val _time = MutableLiveData(Constants.MCQ_TIMER)
    val time: LiveData<Int> get() = _time

    private lateinit var timer: Observable<Long>
    private lateinit var timerCompositeDisposable: CompositeDisposable



    init {
        getAllQuestions()
        prepareTimer()
    }

    private fun getAllQuestions() {
        repository.getAllQuestions().run {
            observeOnMainThread()
            subscribe(::onGetQuestionsSuccess, ::onGetQuestionsError)
        }.add(compositeDisposable)
    }

    private fun onGetQuestionsSuccess(state: State<QuestionResponse>) {
        if (state is State.Success) {
            sortQuestionsAccordingToDifficulty(state)
        } else {
            _requestState.postValue(state)
        }
    }

    private fun onGetQuestionsError(throwable: Throwable) {
        _requestState.postValue(State.Error(requireNotNull(throwable.message)))
    }

    private val allMCQsList = mutableListOf<Question>()
    private val forReplaceMCQsList = mutableListOf<Question>()

    private fun sortQuestionsAccordingToDifficulty(state: State<QuestionResponse>) {
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

    private fun sortQuestionsAccordingToPriority(questionsList: List<Question>) {
        questionsList.subList(0, 5).forEach { allMCQsList.add(it) }
            .also { forReplaceMCQsList.add(questionsList.last()) }
    }

    private fun onAllQuestionsSortedSuccessfully(state: State<QuestionResponse>) {
        startTimer()
        _requestState.postValue(state)
        setCurrentQuestion(allMCQsList.first())
    }

    private fun setCurrentQuestion(quiz: Question) {
        _currentQuestion.postValue(quiz)
        setCurrentQuestionAnswers(quiz)
    }

    private fun setCurrentQuestionAnswers(quiz: Question) {
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
        cancelTimer()
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
        _correctAnswersCount.postValue(_correctAnswersCount.value?.plus(1))
        _score.postValue(_currentQuestionIndex.value?.let { setScore(it) })
        if (isNotLastQuestion()){
            goToNextQuestion()
        } else {
            endGame()
        }
    }

    private fun onAnswerWrongly(answer: Answer) {
        _currentQuestionAnswers.postValue(_currentQuestionAnswers.value?.apply {
            answer.state = AnswerState.SELECTED_INCORRECT
            endGame()
            this.filter { it.isCorrect }.forEach { it.state = AnswerState.SELECTED_CORRECT }
        })
    }

    private fun setScore(currentMCQIndex: Int): Int {
        return Constants.SCORE_LIST[currentMCQIndex]
    }

    private fun isNotLastQuestion(): Boolean {
        return requireNotNull(currentQuestionIndex.value) < allMCQsList.lastIndex
    }

    private fun goToNextQuestion() {
        startTimer()
        _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)
        doAfterDelay(Constants.ONE_SECOND) {
            _isQuestionClickable.postValue(true)
            _currentQuestionIndex.value?.let { setCurrentQuestion(allMCQsList[it]) }
        }
    }



    private fun endGame() {
        doAfterDelay(Constants.ONE_SECOND) {
            if (requireNotNull(_correctAnswersCount.value ) > Constants.MINIMUM_REQUIRED_CORRECT_ANSWERS_TO_PASS){
                _isGameOver.postEvent(GameState.WIN)
            }else{
                _isGameOver.postEvent(GameState.LOSS)
            }

        }
    }

    fun onReplaceQuestion() {
        cancelTimer()
        _currentQuestion.value?.let {
            when (it.difficulty) {
                QuestionDifficulty.EASY.name.lowercase() -> replaceQuestion(forReplaceMCQsList[Constants.FOR_REPLACE_EASY_MCQ_INDEX])
                QuestionDifficulty.MEDIUM.name.lowercase() -> replaceQuestion(forReplaceMCQsList[Constants.FOR_REPLACE_MEDIUM_MCQ_INDEX])
                QuestionDifficulty.HARD.name.lowercase() -> replaceQuestion(forReplaceMCQsList[Constants.FOR_REPLACE_HARD_MCQ_INDEX])
            }
        }
    }

    private fun replaceQuestion(newQuestion: Question) {
        startTimer()
        showAllAnswersStates()
        _isReplaceQuestionUsed.postValue(true)
        doAfterDelay(Constants.ONE_SECOND) {
            _isQuestionClickable.postValue(true)
            currentQuestionIndex.value?.let {
                allMCQsList.replaceAtIndex(it, newQuestion)
                setCurrentQuestion(allMCQsList[it])
            }
        }
    }

    fun onDelete2Answers() {
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
        timer = Observable.interval(0,1,TimeUnit.SECONDS).take(Constants.MCQ_TIMER + 1.toLong()).map {
            Constants.MCQ_TIMER - it
        }.observeOnMainThread()
    }

    private fun startTimer() {
        timerCompositeDisposable = CompositeDisposable()
        timer.subscribe(::onNext, ::onError, ::onComplete).add(timerCompositeDisposable)
    }

    private fun onNext(count: Long) {
        _time.postValue(count.toInt())
    }

    private fun onError(e: Throwable) {
        reportError(Exception(Constants.TIMER_ERROR_MESSAGE))
    }

    private fun onComplete() {
        showAllAnswersStates()
        endGame()
    }

    private fun cancelTimer() {
        timerCompositeDisposable.dispose()
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
        _requestState.postValue(State.Error(error.message.toString()))
    }

    fun onClickExitButton() {
        _openExitDialog.postEvent(true)
    }

    fun tryPlayingAgain() {
        _requestState.postValue(State.Loading)
        getAllQuestions()
    }
}