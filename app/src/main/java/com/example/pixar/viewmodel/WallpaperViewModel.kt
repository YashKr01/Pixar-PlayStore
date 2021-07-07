package com.example.pixar.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pixar.model.DownloadResponse
import com.example.pixar.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class WallpaperViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    private val data = MutableLiveData<Response<DownloadResponse>>()

    fun trackDownloads(url: String): MutableLiveData<Response<DownloadResponse>> {
        viewModelScope.launch {
            val response = repository.trackDownloads(url)
            if (response != null && response.isSuccessful && response.code() == 200) data.postValue(
                response
            )
            else data.postValue(null)
        }

        return data
    }

}