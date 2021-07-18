package com.example.pixar.bottomsheets

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pixar.databinding.BottomSheetImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ImageBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetImageBinding? = null
    private val binding get() = _binding!!

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

        }

        binding.textCamera.setOnClickListener {

        }

    }

//    private fun redirectToPermissionScreen() {
//        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//        val uri = Uri.fromParts("package", requireContext().packageName, null)
//        intent.data = uri;
//        startActivity(intent);
//    }

}