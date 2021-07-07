package com.example.pixar.network

import com.example.pixar.model.DownloadResponse
import com.example.pixar.model.UnsplashResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("client_id") id: String
    ): UnsplashResponse

    @GET
    suspend fun trackDownload(
        @Url url: String,
        @Query("client_id") id: String
    ): Response<DownloadResponse>?

}