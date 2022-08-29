package com.devfalah.quiz.ui.mcq


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.devfalah.quiz.data.repository.QuizRepositoryImp
import com.devfalah.quiz.data.response.QuizResponse
import com.devfalah.quiz.data.response.Quiz
import com.devfalah.quiz.data.service.WebRequest
import com.devfalah.quiz.utilities.McqDifficulty
import com.devfalah.quiz.utilities.State
import com.devfalah.quiz.utilities.addAll
import com.devfalah.quiz.utilities.observeOnMainThread
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

class McqViewModel : ViewModel() {
    private val repository = QuizRepositoryImp(WebRequest().apiService)

    private var _requestState = MutableLiveData<State<QuizResponse>>(State.Loading)
    val requestState get() : LiveData<State<QuizResponse>> = _requestState
    private val questions = mutableListOf<Quiz>()


    private var _currentQuestionIndex = MutableLiveData<Int>(0)
    val currentQuestionIndex get() : LiveData<Int> = _currentQuestionIndex

    private var _allQuestionsSize = MutableLiveData<Int>(0)
    val allQuestionsSize get() : LiveData<Int> = _allQuestionsSize
    private val _currentQuestion = MutableLiveData<Quiz>()
    val currentQuestion get() : LiveData<Quiz> = _currentQuestion

    private val _currentQuestionAnswers = MutableLiveData<List<String>?>()
    val currentQuestionAnswers: LiveData<List<String>?> get() = _currentQuestionAnswers

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> = _score


    init {
        getFifteenQuestions()
    }


    fun onClickAnswer(answer: String) {
        goToNextQuestion()

    }


    private fun getFifteenQuestions() {
        Observable.concat(
            getFiveQuestions(McqDifficulty.EASY).toObservable(),
            getFiveQuestions(McqDifficulty.MEDIUM).toObservable(),
            getFiveQuestions(McqDifficulty.HARD).toObservable()
        ).run {
            observeOnMainThread()
            subscribe(::onGetMCQsSuccess, ::onGetMCQsError)
        }
    }

    private fun onGetMCQsSuccess(state: State<QuizResponse>) {
        val result = requireNotNull(state.toData()?.questions)
        questions.addAll(result)
        when (result.first()?.difficulty) {
            McqDifficulty.HARD.name.lowercase() -> {
                if (state is State.Success) {
                    _requestState.postValue(state)
                    setQuestion(questions.first())
                    _allQuestionsSize.postValue(questions.size)
                }
            }
        }
    }


    private fun setQuestion(quiz: Quiz) {
        _currentQuestion.postValue(quiz)
        _currentQuestionAnswers.postValue(quiz.incorrectAnswers?.plus(quiz.correctAnswer)
            ?.shuffled() as List<String>)
    }

    private fun goToNextQuestion() {
        incrementCurrentQuestionIndex()
        if (questions.size > _currentQuestionIndex.value!!) {
            setQuestion(questions[_currentQuestionIndex.value!!])
        } else {
            _score.postValue(0)
        }
    }

    private fun incrementCurrentQuestionIndex() {
        _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)!!
    }

    private fun onGetMCQsError(throwable: Throwable) =
        _requestState.postValue(State.Error(requireNotNull(throwable.message)))

    private fun getFiveQuestions(difficulty: McqDifficulty): Single<State<QuizResponse>> =
        repository.getQuizQuestions(difficulty)

}


