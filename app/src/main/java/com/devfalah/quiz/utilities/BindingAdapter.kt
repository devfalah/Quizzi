package com.devfalah.quiz.utilities

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.devfalah.quiz.R
import com.devfalah.quiz.domain.enums.AnswerState
import com.devfalah.quiz.domain.enums.GameState
import com.google.android.material.card.MaterialCardView

@BindingAdapter(value = ["app:showWhenSuccess"])
fun <T> showWhenSuccess(view: View, state: State<T>?) {
    view.isVisible = (state is State.Success)
}

@BindingAdapter(value = ["app:showWhenError"])
fun <T> showWhenError(view: View, state: State<T>?) {
    view.isVisible = (state is State.Error)
}


@BindingAdapter(value = ["app:showWhenLoading"])
fun <T> showWhenLoading(view: View, state: State<T>?) {
    view.isVisible = (state is State.Loading)
}

@BindingAdapter(value = ["isVisible"])
fun showIfTrue(view: View, status: Boolean) {
    view.isVisible = status
}


@BindingAdapter(value = ["app:progressBarDrawable"])
fun setProgressBarDrawable(view: ProgressBar, value: Int?) {
    view.progressDrawable = if (value!!.toInt() > 10) ContextCompat.getDrawable(
        view.context,
        R.drawable.circle_progress_bar
    ) else ContextCompat.getDrawable(view.context, R.drawable.red_progressbar)
}

@BindingAdapter(value = ["app:setAnswerBackgroundColor"])
fun setAnswerBackgroundColor(view: MaterialCardView, state: AnswerState?) {
    when (state) {
        null,
        AnswerState.UNSELECTED,
        -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
            view.strokeWidth = 0
        }
        AnswerState.SELECTED_CORRECT -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.green))
        }
        AnswerState.TIMEOUT_CORRECT -> {
            view.strokeColor = ContextCompat.getColor(view.context, R.color.green)
            view.strokeWidth = 3
        }
        AnswerState.TIMEOUT_INCORRECT -> {
            view.strokeColor = ContextCompat.getColor(view.context, R.color.red)
            view.strokeWidth = 3
        }
        AnswerState.SELECTED_INCORRECT -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
        }
    }
}

@BindingAdapter(value = ["app:setAnswerBodyTextStyle"])
fun setAnswerBodyTextStyle(view: TextView, state: AnswerState?) {
    when (state) {
        null,
        AnswerState.UNSELECTED,
        -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedBody)
        }
        AnswerState.SELECTED_CORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedBody)
        }
        AnswerState.SELECTED_INCORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedBody)
        }
        AnswerState.TIMEOUT_CORRECT,
        AnswerState.TIMEOUT_INCORRECT,
        -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedBody)
        }
    }
}

@BindingAdapter(value = ["app:setAnswerAlphabetTextStyle"])
fun setAnswerAlphabetTextStyle(view: TextView, state: AnswerState?) {
    when (state) {
        null,
        AnswerState.UNSELECTED,
        -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedAlphabet)
            view.setBackgroundResource(R.drawable.circle)
        }
        AnswerState.SELECTED_CORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedAlphabet_Correct)
            view.setBackgroundResource(R.drawable.circle_white)
        }
        AnswerState.TIMEOUT_CORRECT,
        AnswerState.TIMEOUT_INCORRECT,
        -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedAlphabet)
            view.setBackgroundResource(R.drawable.circle)
        }
        AnswerState.SELECTED_INCORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedAlphabet_Wrong)
            view.setBackgroundResource(R.drawable.circle_white)
        }
    }
}

@BindingAdapter(value = ["setResultLottieAnimation"])
fun setResultLottieAnimation(view: LottieAnimationView, gameState: GameState?) {
    when (gameState) {
        null,
        GameState.WIN,
        -> {
            view.setAnimation(R.raw.congrats)
        }
        GameState.LOSS -> {
            view.setAnimation(R.raw.failed)
        }
    }

}

@BindingAdapter(value = ["setDecodedString"])
fun setDecodedString(view: TextView, value: String?) {
    view.text = value?.decodeHtml()
}