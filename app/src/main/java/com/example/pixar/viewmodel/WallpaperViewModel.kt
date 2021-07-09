package com.example.pixar.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pixar.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {


}