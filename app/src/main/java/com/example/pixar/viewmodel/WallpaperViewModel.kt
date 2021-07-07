package com.example.pixar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixar.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    fun trackDownloads(url: String) {
        viewModelScope.launch {
            repository.trackDownloads(url)
        }
    }

}