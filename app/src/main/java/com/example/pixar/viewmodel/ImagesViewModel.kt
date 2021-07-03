package com.example.pixar.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.example.pixar.repository.UnsplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val repository: UnsplashRepository
) : ViewModel() {

    private val currentQuery = MutableLiveData(DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getPixabayPhotos(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String) {
        currentQuery.value = query
    }

    companion object {
        private const val DEFAULT_QUERY = "wallpaper"
    }

}