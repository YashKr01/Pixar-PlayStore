package com.example.pixar.bottomsheets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pixar.databinding.WallpaperBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WallpaperBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: WallpaperBottomSheetBinding? = null
    private val binding get() = _binding!!

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

    }

}