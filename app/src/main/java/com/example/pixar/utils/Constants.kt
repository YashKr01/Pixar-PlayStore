package com.example.pixar.utils

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

class Constants {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "tLc3BkhA9BbLGzziRGNpc4mHem6AKRgUxIX5K8mcemw"

        fun showSnackBar(context: Context, view: View, message: String, duration: Int) {
            Snackbar.make(context, view, message, duration)
        }

    }

}