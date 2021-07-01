package com.example.pixar.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.net.HttpURLConnection
import java.net.URL

class Constants {

    companion object {
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "tLc3BkhA9BbLGzziRGNpc4mHem6AKRgUxIX5K8mcemw"
        const val UNSPLASH_URL = "https://unsplash.com/?utm_source=Pixar&utm_medium=referral"

        fun showSnackBar(context: Context, view: View, message: String, duration: Int) {
            Snackbar.make(context, view, message, duration)
        }

        fun imageToBitmap(imageUrl: String): Bitmap? {

            return try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                BitmapFactory.decodeStream(connection.inputStream)
            } catch (e: Exception) {
                Log.d("TAG", "convertToBitmap: $e")
                null
            }

        }

    }

}