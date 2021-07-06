package com.example.pixar.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.example.pixar.databinding.WallpaperBottomSheetBinding
import com.example.pixar.utils.Constants
import com.example.pixar.utils.Constants.Companion.isOnline
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

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

            when {
                binding.radioButtonHome.isChecked -> {
                    Toast.makeText(requireContext(), "HOME", Toast.LENGTH_SHORT).show()
                }
                binding.radioButtonLock.isChecked -> {
                    Toast.makeText(requireContext(), "LOCK", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Toast.makeText(requireContext(), "BOTH", Toast.LENGTH_SHORT).show()
                }
            }

        }

    }


}