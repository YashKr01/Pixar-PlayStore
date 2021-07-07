package com.example.pixar.bottomsheets

import android.app.WallpaperManager
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pixar.R
import com.example.pixar.databinding.WallpaperBottomSheetBinding
import com.example.pixar.utils.Constants
import com.example.pixar.utils.Constants.Companion.imageToBitmap
import com.example.pixar.utils.Constants.Companion.isOnline
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class WallpaperBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: WallpaperBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<WallpaperBottomSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WallpaperBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = args.photo

        binding.buttonSetWallpaper.setOnClickListener {

            if (isOnline(requireContext())) {

                when {

                    // if only home screen is selected
                    binding.radioButtonHome.isChecked -> {

                        lifecycleScope.launch(Dispatchers.IO) {

                            val bitmap = imageToBitmap(photo.urls.small)

                            if (bitmap != null) {
                                val e = setWallpaperOnHomeScreen(bitmap)
                                if (e != null) withContext(Dispatchers.Main) {
                                    snackBar(e)
                                } else {
                                    withContext(Dispatchers.Main) {
                                        snackBar(getString(R.string.wallpaper_has_been_set))
                                        withContext(Dispatchers.IO) { delay(1000L) }
                                        dismiss()
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    snackBar(getString(R.string.an_error_occurred))
                                }
                            }
                        }

                    }

                    // if only lock screen is selected
                    binding.radioButtonLock.isChecked -> {

                        lifecycleScope.launch(Dispatchers.IO) {

                            val bitmap = imageToBitmap(photo.urls.small)

                            if (bitmap != null) {
                                val e = setWallpaperOnLockScreen(bitmap)
                                if (e != null) withContext(Dispatchers.Main) {
                                    snackBar(e)
                                } else {
                                    withContext(Dispatchers.Main) {
                                        snackBar(getString(R.string.wallpaper_has_been_set))
                                        withContext(Dispatchers.IO) { delay(1000L) }
                                        dismiss()
                                    }
                                }
                            } else {
                                withContext(Dispatchers.Main) {
                                    snackBar(getString(R.string.an_error_occurred))
                                }
                            }

                        }

                    }

                    // if both are selected
                    else -> {

                        lifecycleScope.launch(Dispatchers.IO) {

                            val bitmap = imageToBitmap(photo.urls.small)

                            if (bitmap != null) {

                                setWallpaperOnHomeScreen(bitmap)
                                setWallpaperOnLockScreen(bitmap)

                                withContext(Dispatchers.Main) {
                                    snackBar(getString(R.string.wallpaper_has_been_set))
                                    withContext(Dispatchers.IO) { delay(1000L) }
                                    dismiss()
                                }

                            } else {
                                withContext(Dispatchers.Main) { snackBar(getString(R.string.an_error_occurred)) }
                            }

                        }

                    }
                }
            } else {
                snackBar(getString(R.string.no_connection))
            }

        }

        binding.buttonCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun setWallpaperOnHomeScreen(bitmap: Bitmap): String? {
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        return try {
            wallpaperManager.setBitmap(bitmap)
            null
        } catch (e: IOException) {
            e.message.toString()
        }
    }

    private fun setWallpaperOnLockScreen(bitmap: Bitmap): String? {
        val wallpaperManager = WallpaperManager.getInstance(requireContext())
        return try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            }
            null
        } catch (e: IOException) {
            e.message.toString()
        }
    }

    private fun snackBar(message: String) {
        Constants.showSnackBar(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)
    }

}