package com.danda.danda.util

import android.content.Context
import android.view.View
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) =
    Toast.makeText(applicationContext, message , duration).show()

fun showLoading(isLoading: Boolean, progressBar: LottieAnimationView) {
    when {
        isLoading -> View.VISIBLE
        else -> View.GONE
    }.also { progressBar.visibility = it }
}