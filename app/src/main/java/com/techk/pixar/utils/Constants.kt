package com.techk.pixar.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.techk.pixar.R
import com.techk.pixar.model.Category
import com.techk.pixar.model.ViewPagerModel
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL

class Constants {

    companion object {

        // URLS and API KEYS
        const val BASE_URL = "https://api.unsplash.com/"
        const val CLIENT_ID = "tLc3BkhA9BbLGzziRGNpc4mHem6AKRgUxIX5K8mcemw"
        const val UNSPLASH_URL = "https://unsplash.com/?utm_source=Pixar&utm_medium=referral"
        const val IMAGE_DOWNLOAD_FOLDER_NAME = "Pixar"

        const val DEFAULT = "patterns"

        const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.techk.pixar"

        // Methods
        fun showSnackBar(context: Context, view: View, message: String, duration: Int) =
            Snackbar.make(context, view, message, duration).show()

        fun imageToBitmap(imageUrl: String): Bitmap? {

            return try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                BitmapFactory.decodeStream(connection.inputStream)
            } catch (e: Exception) {
                null
            }

        }

        fun isOnline(context: Context): Boolean {

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }

            return false
        }

        fun initViewPagerList(viewPagerList: ArrayList<ViewPagerModel>, context: Context) {
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_search,
                    context.getString(R.string.vp_search),
                    context.getString(R.string.search_and_download)
                )
            )
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_edit,
                    context.getString(R.string.vp_edit),
                    context.getString(R.string.edit_photo)
                )
            )
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_rating,
                    context.getString(R.string.vp_rate),
                    context.getString(R.string.rate_app)
                )
            )
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_share,
                    context.getString(R.string.vp_share),
                    context.getString(R.string.share_app)
                )
            )
        }

        fun initList(list: ArrayList<Category>, context: Context) {
            list.clear()
            list.add(Category(context.getString(R.string.nature), R.drawable.ic_nature))
            list.add(Category(context.getString(R.string.wildlife), R.drawable.ic_wildlife))
            list.add(Category(context.getString(R.string.sports), R.drawable.ic_sports))
            list.add(Category(context.getString(R.string.food), R.drawable.ic_food))
            list.add(Category(context.getString(R.string.cities), R.drawable.ic_cities))
            list.add(Category(context.getString(R.string.patterns), R.drawable.ic_patterns))
        }

    }

}