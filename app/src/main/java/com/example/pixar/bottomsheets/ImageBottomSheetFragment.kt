package com.example.pixar.bottomsheets

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.example.pixar.R
import com.example.pixar.databinding.BottomSheetImageBinding
import com.example.pixar.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import java.io.File

class ImageBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetImageBinding? = null
    private val binding get() = _binding!!

    private lateinit var uri: Uri

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val intent = Intent(requireContext(), DsPhotoEditorActivity::class.java).apply {
                    data = uri
                    putExtra(
                        DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                        Constants.IMAGE_DOWNLOAD_FOLDER_NAME
                    )
                }
                startActivity(intent)
            }
        }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                val intent = Intent(requireContext(), DsPhotoEditorActivity::class.java).apply {
                    data = uri
                    putExtra(
                        DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                        Constants.IMAGE_DOWNLOAD_FOLDER_NAME
                    )
                }
                startActivity(intent)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storagePermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {
                    pickImage.launch("image/*")
                } else displaySnackBar()
            }

        val cameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {

                    val photoFile = File.createTempFile(
                        "IMG_",
                        ".jpg",
                        requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                    )

                    uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.provider",
                        photoFile
                    )
                    takePicture.launch(uri)

                } else displaySnackBar()
            }

        binding.textImage.setOnClickListener {
            storagePermission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.textCamera.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }

    }

    private fun displaySnackBar() {
        Snackbar.make(
            requireContext(),
            binding.root,
            "Permission Required",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("Allow") {
            redirectToPermissionScreen()
        }
            .setActionTextColor(resources.getColor(R.color.colorYellow, null))
            .show()
    }

    private fun redirectToPermissionScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}