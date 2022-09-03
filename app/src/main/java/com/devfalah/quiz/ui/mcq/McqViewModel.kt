package com.devfalah.quiz.ui.mcq

import androidx.lifecycle.*
import com.devfalah.quiz.data.model.Answer
import com.devfalah.quiz.data.model.Quiz
import com.devfalah.quiz.data.model.QuizResponse
import com.devfalah.quiz.data.repository.QuizRepositoryImp
import com.devfalah.quiz.data.service.WebRequest
import com.devfalah.quiz.utilities.*
import com.devfalah.quiz.utilities.enums.AnswerState
import com.devfalah.quiz.utilities.enums.McqDifficulty
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class McqViewModel : ViewModel() {
    private val repository = QuizRepositoryImp(WebRequest().apiService)

    private val _requestState = MutableLiveData<State<QuizResponse>>(State.Loading)
    val requestState: LiveData<State<QuizResponse>> get() = _requestState

    private val _currentMCQ = MutableLiveData<Quiz>()

    private val _currentDecodedMCQ = MutableLiveData<String>()
    val currentDecodedMCQ: LiveData<String> get() = _currentDecodedMCQ

    private val _currentMCQIndex = MutableLiveData(0)
    val currentMCQIndex: LiveData<Int> get() = _currentMCQIndex

    private val _currentMCQAnswers = MutableLiveData<List<Answer>>()
    val currentMCQAnswers: LiveData<List<Answer>> get() = _currentMCQAnswers

    private var _correctAnswersCount = MutableLiveData(0)
    val correctAnswersCount: LiveData<Int> get() = _correctAnswersCount

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _isGameOver = MutableLiveData<Boolean>()
    val isGameOver: LiveData<Boolean> get() = _isGameOver

    private val _isReplaceMCQUsed = MutableLiveData(false)
    val isReplaceMCQUsed: LiveData<Boolean> get() = _isReplaceMCQUsed

    private val _isDelete2AnswersUsed = MutableLiveData(false)
    val isDelete2AnswersUsed: LiveData<Boolean> get() = _isDelete2AnswersUsed

    private val _time = MutableLiveData(Constants.MCQ_TIMER)
    val time: LiveData<Int> get() = _time

    private lateinit var timer: CountdownTimer

    private val _isMCQsClickable = MutableLiveData(true)
    val isMCQsClickable: LiveData<Boolean> get() = _isMCQsClickable


    init {
        getAllMCQs()
        prepareTimer()
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

    private fun getMCQs(difficulty: McqDifficulty): Single<State<QuizResponse>> =
        repository.getQuizQuestions(difficulty)

    private fun onGetMCQsSuccess(state: State<QuizResponse>) =
        if (state is State.Success) sortMCQsAccordingToDifficulty(state) else _requestState.postValue(
            state
        )

    private fun onGetMCQsError(throwable: Throwable) =
        _requestState.postValue(State.Error(requireNotNull(throwable.message)))

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

    private fun sortMCQsAccordingToPriority(mcqList: List<Quiz?>) =
        mcqList.subList(0, 5).forEach { allMCQsList.add(it!!) }
            .also { forReplaceMCQsList.add(mcqList.last()!!) }

    private fun onAllMCQsSortedSuccessfully(state: State<QuizResponse>) {
        timer.start()
        _requestState.postValue(state)
        setCurrentMCQ(allMCQsList.first())
    }

    private fun setCurrentMCQ(quiz: Quiz) {
        _currentMCQ.postValue(quiz)
        _currentDecodedMCQ.postValue(quiz.question!!.decodeHtml())
        setCurrentMCQAnswers(quiz)
    }

    private fun setCurrentMCQAnswers(quiz: Quiz) {
        val listOfAnswers = quiz.incorrectAnswers!!.map { it!!.toMCQAnswer(false) }
            .plus(quiz.correctAnswer!!.toMCQAnswer(true))
//            .shuffled()
            .toMutableList()
        _currentMCQAnswers.postValue(listOfAnswers)
    }

    fun onAnswerClick(answer: Answer) {
        timer.dispose()
         _isMCQsClickable.postValue(false)
        if (answer.isCorrect) onAnswerCorrectly(answer) else onAnswerWrongly(answer)
        if (isNotLastQuestion()) goToNextMCQ() else endGame()
    }

    private fun onAnswerCorrectly(answer: Answer) {
        _currentMCQAnswers.postValue(_currentMCQAnswers.value?.apply { answer.state = AnswerState.SELECTED_CORRECT })
        _correctAnswersCount.value = _correctAnswersCount.value!! + 1
        _score.postValue(setScore(_correctAnswersCount.value!!))
    }

    private fun setScore(correctAnswersCount: Int): Int{
        return when(correctAnswersCount){
            1 -> Constants.SCORE_500
            2 -> Constants.SCORE_1000
            3 -> Constants.SCORE_2000
            4 -> Constants.SCORE_3000
            5 -> Constants.SCORE_5000
            6 -> Constants.SCORE_7500
            7 -> Constants.SCORE_10000
            8 -> Constants.SCORE_12500
            9 -> Constants.SCORE_15000
            10 -> Constants.SCORE_25000
            11 -> Constants.SCORE_50000
            12 -> Constants.SCORE_100000
            13 -> Constants.SCORE_250000
            14 -> Constants.SCORE_500000
            15 -> Constants.SCORE_1000000
            else -> 0
        }
    }
    private fun onAnswerWrongly(answer: Answer) = _currentMCQAnswers.postValue(_currentMCQAnswers.value?.apply {
        answer.state = AnswerState.SELECTED_INCORRECT
        this.filter { it.isCorrect }.forEach { it.state = AnswerState.SELECTED_CORRECT }
    })

    private fun isNotLastQuestion(): Boolean = currentMCQIndex.value!! < allMCQsList.lastIndex

    private fun goToNextMCQ() {
        timer.dispose()
        if (currentMCQIndex.value!! < allMCQsList.lastIndex) {
            viewModelScope.launch {
                timer.start()
                _currentMCQIndex.value = _currentMCQIndex.value!! + 1
                delay(1000)
                _isMCQsClickable.postValue(true)
                setCurrentMCQ(allMCQsList[_currentMCQIndex.value!!])
            }
        } else endGame()

    }

    private fun endGame() = _isGameOver.postValue(true)

    fun onReplaceMCQClickListener() {
        timer.dispose()
        when (_currentMCQ.value!!.difficulty) {
            McqDifficulty.EASY.name.lowercase() -> replaceMCQ(forReplaceMCQsList[Constants.FOR_REPLACE_EASY_MCQ_INDEX])
            McqDifficulty.MEDIUM.name.lowercase() -> replaceMCQ(forReplaceMCQsList[Constants.FOR_REPLACE_MEDIUM_MCQ_INDEX])
            McqDifficulty.HARD.name.lowercase() -> replaceMCQ(forReplaceMCQsList[Constants.FOR_REPLACE_HARD_MCQ_INDEX])
        }
    }

    private fun replaceMCQ(newMCQ: Quiz) {
        viewModelScope.launch {
            timer.start()
            changeAnswersStateOnTimeOut()
            delay(1000)
            allMCQsList.replaceAtIndex(currentMCQIndex.value!!, newMCQ)
            setCurrentMCQ(allMCQsList[currentMCQIndex.value!!])
            _isReplaceMCQUsed.postValue(true)
        }


    }

    fun onDelete2AnswersClickListener() {
         val correctAnswer = _currentMCQAnswers.value!!.first { it.isCorrect }
         val incorrectAnswer = _currentMCQAnswers.value!!.first { !it.isCorrect }
        _currentMCQAnswers.postValue(listOf(correctAnswer, incorrectAnswer).shuffled())
        _isDelete2AnswersUsed.postValue(true)
    }

    private fun prepareTimer() {
        timer = object : CountdownTimer(Constants.MCQ_TIMER.toLong(), TimeUnit.SECONDS) {
            override fun onTick(tickValue: Long) = _time.postValue(tickValue.toInt())
            override fun onFinish() {
                changeAnswersStateOnTimeOut()
                goToNextMCQ()
            }
        }
    }

    private fun changeAnswersStateOnTimeOut() {
        _currentMCQAnswers.postValue(_currentMCQAnswers.value?.onEach {
            if (it.isCorrect) it.state = AnswerState.TIMEOUT_CORRECT
            else it.state = AnswerState.TIMEOUT_INCORRECT
        })
    }

    fun tryPlayingAgain() {
        _requestState.postValue(State.Loading)
        getAllMCQs()
    }
}