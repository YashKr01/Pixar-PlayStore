package com.example.pixar.bottomsheets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.example.pixar.databinding.BottomSheetImageBinding
import com.example.pixar.utils.Constants
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class ImageBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetImageBinding? = null
    private val binding get() = _binding!!

    private val storagePermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                pickImage.launch("image/*")
            } else displaySnackBar()
        }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

            if (uri != null) {
                val intent = Intent(requireContext(), DsPhotoEditorActivity::class.java)
                intent.data = uri
                intent.putExtra(
                    DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                    Constants.IMAGE_DOWNLOAD_FOLDER_NAME
                )
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

        binding.textImage.setOnClickListener {
            storagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        binding.textCamera.setOnClickListener {

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
        }.show()
    }

    private fun redirectToPermissionScreen() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireContext().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

}