package com.example.pixar.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.pixar.network.ApiInterface
import com.example.pixar.paging.UnSplashPagingSource
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun getResults(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            maxSize = 100,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UnSplashPagingSource(apiInterface, query) }
    ).liveData

}