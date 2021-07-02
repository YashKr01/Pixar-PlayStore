package com.example.pixar.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.material.snackbar.Snackbar
import java.net.HttpURLConnection
import java.net.URL


class Constants {

    companion object {

        // URLS and API KEYS
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "tLc3BkhA9BbLGzziRGNpc4mHem6AKRgUxIX5K8mcemw"
        const val UNSPLASH_URL = "https://unsplash.com/?utm_source=Pixar&utm_medium=referral"

        // Methods
        fun showSnackBar(context: Context?, view: View, message: String, duration: Int) {
            Snackbar.make(context!!, view, message, duration).show()
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

        fun isOnline(context: Context?): Boolean {

            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
            return false
        }

    }


}