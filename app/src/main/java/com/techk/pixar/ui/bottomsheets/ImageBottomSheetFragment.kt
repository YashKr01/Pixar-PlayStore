package com.techk.pixar.ui.bottomsheets

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
import com.techk.pixar.R
import com.techk.pixar.databinding.BottomSheetImageBinding
import com.techk.pixar.utils.Constants
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
                if (granted) pickImage.launch(getString(R.string.image_intent))
                else displaySnackBar()
            }

        val cameraPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (granted) {

                    val photoFile = File.createTempFile(
                        getString(R.string.image_uri),
                        getString(R.string.suffix_uri),
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
            getString(R.string.permission_required),
            Snackbar.LENGTH_INDEFINITE
        ).setAction(getString(R.string.allow)) {
            redirectToPermissionScreen()
        }
            .setActionTextColor(resources.getColor(R.color.colorYellow, null))
            .show()
    }

    private fun redirectToPermissionScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

        val uri = Uri.fromParts(
            getString(R.string.intent_package),
            requireContext().packageName,
            null
        )

        intent.data = uri
        startActivity(intent)
    }

}