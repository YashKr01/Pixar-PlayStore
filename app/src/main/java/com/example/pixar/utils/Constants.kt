package com.example.pixar.utils

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Environment
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.example.pixar.R
import com.example.pixar.model.Category
import com.example.pixar.model.ViewPagerModel
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

        // Permissions Codes
        const val PERMISSION_WRITE_STORAGE_CODE = 1

        // Methods
        fun showSnackBar(context: Context, view: View, message: String, duration: Int) {
            Snackbar.make(context, view, message, duration).show()
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

        fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {

            if (android.os.Build.VERSION.SDK_INT >= 29) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folderName")
                values.put(MediaStore.Images.Media.IS_PENDING, true)
                // RELATIVE_PATH and IS_PENDING are introduced in API 29.

                val uri: Uri? = context.contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
                if (uri != null) {
                    saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                    values.put(MediaStore.Images.Media.IS_PENDING, false)
                    context.contentResolver.update(uri, values, null, null)
                }

            } else {
                val directory =
                    File(Environment.getExternalStorageState().toString() + separator + folderName)
                // getExternalStorageDirectory is deprecated in API 29

                if (!directory.exists()) directory.mkdirs()

                val fileName = System.currentTimeMillis().toString() + ".png"
                val file = File(directory, fileName)

                saveImageToStream(bitmap, FileOutputStream(file))

                val values = contentValues()
                values.put(MediaStore.Images.Media.DESCRIPTION, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        }

        private fun contentValues(): ContentValues {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000)
            return values
        }

        private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
            if (outputStream != null) {
                try {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        fun initViewPagerList(viewPagerList: ArrayList<ViewPagerModel>, context: Context) {
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_search,
                    context.getString(R.string.vp_search)
                )
            )
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_edit,
                    context.getString(R.string.vp_edit)
                )
            )
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_rating,
                    context.getString(R.string.vp_rate)
                )
            )
            viewPagerList.add(
                ViewPagerModel(
                    R.drawable.vp_share,
                    context.getString(R.string.vp_share)
                )
            )
        }

        fun initList(list: ArrayList<Category>) {
            list.clear()
            list.add(Category("Nature", R.drawable.ic_nature))
            list.add(Category("Wildlife", R.drawable.ic_wildlife))
            list.add(Category("Sports", R.drawable.ic_sports))
            list.add(Category("Food", R.drawable.ic_food))
            list.add(Category("Cities", R.drawable.ic_cities))
        }

    }

}