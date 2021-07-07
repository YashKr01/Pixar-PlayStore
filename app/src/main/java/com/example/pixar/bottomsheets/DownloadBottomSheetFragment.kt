package com.example.pixar.bottomsheets

import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pixar.R
import com.example.pixar.databinding.DownloadBottomSheetBinding
import com.example.pixar.utils.Constants
import com.example.pixar.utils.Constants.Companion.imageToBitmap
import com.example.pixar.utils.Constants.Companion.isOnline
import com.example.pixar.utils.Constants.Companion.saveImage
import com.example.pixar.utils.Constants.Companion.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

class DownloadBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: DownloadBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DownloadBottomSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DownloadBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = args.photo

        binding.buttonDownload.setOnClickListener {
            if (isOnline(requireContext())) {

                val progressDialog = ProgressDialog(requireContext())
                progressDialog.apply {
                    setCancelable(false)
                    setMessage("Downloading")
                }.show()

                if (binding.radioButtonNormal.isChecked) {

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            withTimeout(4000L) {
                                downloadImage(photo.webformatURL)
                                withContext(Dispatchers.Main) {
                                    progressDialog.dismiss()
                                    snackBar(getString(R.string.downloaded_to_gallery))
                                    withContext(Dispatchers.IO) { delay(1000L) }
                                    dismiss()
                                }
                            }
                        } catch (e: TimeoutCancellationException) {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                snackBar(getString(R.string.timeout_error))
                            }
                        }
                    }

                } else {

                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            withTimeout(4000L) {
                                downloadImage(photo.largeImageURL)
                                withContext(Dispatchers.Main) {
                                    progressDialog.dismiss()
                                    snackBar(getString(R.string.downloaded_to_gallery))
                                    withContext(Dispatchers.IO) { delay(1000L) }
                                    dismiss()
                                }
                            }
                        } catch (e: TimeoutCancellationException) {
                            withContext(Dispatchers.Main) {
                                progressDialog.dismiss()
                                snackBar(getString(R.string.timeout_error))
                            }
                        }
                    }

                }

            } else {
                snackBar(getString(R.string.no_connection))
            }
        }

        binding.buttonDownloadCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun snackBar(message: String) {
        showSnackBar(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)
    }

    private fun downloadImage(url: String) {
        val imageBitmap = imageToBitmap(url)
        saveImage(imageBitmap!!, requireContext(), Constants.IMAGE_DOWNLOAD_FOLDER_NAME)
    }

}