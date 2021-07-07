package com.example.pixar.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.pixar.network.ApiInterface
import com.example.pixar.paging.PixabayPagingSource
import com.example.pixar.paging.UnSplashPagingSource
import com.example.pixar.utils.Constants.Companion.CLIENT_ID
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun getResults(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UnSplashPagingSource(apiInterface, query) }
    ).liveData

    fun getPixabayPhotos(query: String) = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PixabayPagingSource(apiInterface, query) }
    ).liveData

    suspend fun trackDownloads(url:String){
        apiInterface.trackDownload(url,CLIENT_ID)
    }

}