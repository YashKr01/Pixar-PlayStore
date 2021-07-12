package com.example.pixar.bottomsheets

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.pixar.R
import com.example.pixar.databinding.DownloadBottomSheetBinding
import com.example.pixar.utils.Constants
import com.example.pixar.utils.Constants.Companion.PERMISSION_WRITE_STORAGE_CODE
import com.example.pixar.utils.Constants.Companion.imageToBitmap
import com.example.pixar.utils.Constants.Companion.isOnline
import com.example.pixar.utils.Constants.Companion.saveImage
import com.example.pixar.utils.Constants.Companion.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class DownloadBottomSheetFragment : BottomSheetDialogFragment(),
    EasyPermissions.PermissionCallbacks {

    private var _binding: DownloadBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<DownloadBottomSheetFragmentArgs>()

    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DownloadBottomSheetBinding.inflate(inflater, container, false)
        alertDialog = AlertDialog.Builder(requireActivity()).create()
        alertDialog.apply {
            setCancelable(false)
            setView(LayoutInflater.from(activity).inflate(R.layout.layout_alert_dialog, container))
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val photo = args.photo

        binding.buttonDownload.setOnClickListener {

            if (isOnline(requireContext())) {

                if (hasStoragePermission()) {
                    alertDialog.show()

                    if (binding.radioButtonNormal.isChecked) {
                        if (hasStoragePermission()) download(photo.urls.small)
                        else requestPermission()
                    } else {
                        if (hasStoragePermission()) download(photo.urls.regular)
                        else requestPermission()
                    }

                } else requestPermission()

            } else snackBar(getString(R.string.no_connection))

        }

        binding.buttonDownloadCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun download(url: String) {

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                withTimeout(4000L) {
                    downloadImage(url)
                    withContext(Dispatchers.Main) {
                        alertDialog.dismiss()
                        snackBar(getString(R.string.downloaded_to_gallery))
                        withContext(Dispatchers.IO) { delay(1000L) }
                        dismiss()
                    }
                }
            } catch (e: TimeoutCancellationException) {
                withContext(Dispatchers.Main) {
                    alertDialog.dismiss()
                    snackBar(getString(R.string.timeout_error))
                }
            }
        }

    }

    private fun snackBar(message: String) =
        showSnackBar(requireContext(), binding.root, message, Snackbar.LENGTH_SHORT)


    private fun downloadImage(url: String) {
        val imageBitmap = imageToBitmap(url)
        saveImage(imageBitmap!!, requireContext(), Constants.IMAGE_DOWNLOAD_FOLDER_NAME)
    }

    private fun hasStoragePermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun requestPermission() =
        EasyPermissions.requestPermissions(
            this,
            getString(R.string.write_storage_rationale),
            PERMISSION_WRITE_STORAGE_CODE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (isOnline(requireContext())) download(args.photo.urls.regular)
        else snackBar(getString(R.string.no_connection))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}