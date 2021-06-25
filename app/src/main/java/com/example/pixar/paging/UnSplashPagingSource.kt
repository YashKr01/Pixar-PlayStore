package com.example.pixar.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixar.model.UnsplashPhoto
import com.example.pixar.network.ApiInterface
import com.example.pixar.utils.Constants.Companion.CLIENT_ID
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnSplashPagingSource(
    private val apiInterface: ApiInterface,
    private val query: String
) : PagingSource<Int, UnsplashPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhoto> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = apiInterface.searchPhotos(query, position, params.loadSize,CLIENT_ID)
            val photos = response.results

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhoto>): Int? {
        TODO("Not yet implemented")
    }

}