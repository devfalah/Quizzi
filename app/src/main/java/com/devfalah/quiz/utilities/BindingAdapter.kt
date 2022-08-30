package com.devfalah.quiz.utilities

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.devfalah.quiz.R


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

@BindingAdapter(value = ["app:setAnswerBackgroundColor"])
fun setAnswerBackgroundColor(view: CardView, state: AnswerState?) {
    when (state) {
        AnswerState.UNSELECTED -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.white))
        }
        AnswerState.CORRECT -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.green))
        }
        AnswerState.TIMEOUT -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.blue))
        }
        AnswerState.INCORRECT -> {
            view.setCardBackgroundColor(ContextCompat.getColor(view.context, R.color.red))
        }
        else -> {}
    }
}

@BindingAdapter(value = ["app:setAnswerBodyTextStyle"])
fun setAnswerBodyTextStyle(view: TextView, state: AnswerState?) {
    when (state) {
        AnswerState.UNSELECTED -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedBody)
        }
        AnswerState.CORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedBody)
        }
        AnswerState.TIMEOUT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedBody)
        }
        AnswerState.INCORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedBody)
        }
        else -> {}
    }
}

@BindingAdapter(value = ["app:setAnswerAlphabetTextStyle"])
fun setAnswerAlphabetTextStyle(view: TextView, state: AnswerState?) {
    when (state) {
        AnswerState.UNSELECTED -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedAlphabet)
            view.setBackgroundResource(R.drawable.circle)
        }
        AnswerState.CORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedAlphabet_Correct)
            view.setBackgroundResource(R.drawable.circle_white)
        }
        AnswerState.TIMEOUT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_NotSelectedAlphabet)
            view.setBackgroundResource(R.drawable.circle)
        }
        AnswerState.INCORRECT -> {
            TextViewCompat.setTextAppearance(view, R.style.ChoiceTextStyle_SelectedAlphabet_Wrong)
            view.setBackgroundResource(R.drawable.circle_white)
        }
        else -> {}
    }
}