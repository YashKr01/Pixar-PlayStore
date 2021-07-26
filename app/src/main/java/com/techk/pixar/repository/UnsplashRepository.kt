package com.techk.pixar.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.techk.pixar.network.ApiInterface
import com.techk.pixar.paging.UnSplashPagingSource
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val apiInterface: ApiInterface) {

    fun getResults(query: String) = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { UnSplashPagingSource(apiInterface, query) }
    ).liveData

}