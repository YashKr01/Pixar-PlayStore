package com.example.pixar.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.pixar.model.PixabayPhoto
import com.example.pixar.model.UnsplashPhoto
import com.example.pixar.network.ApiInterface
import com.example.pixar.utils.Constants
import com.example.pixar.utils.Constants.Companion.PIXABAY_API_KEY
import com.example.pixar.utils.Constants.Companion.PIXABAY_BASE_URL
import retrofit2.HttpException
import java.io.IOException

private const val PIXABAY_STARTING_PAGE = 1

class PixabayPagingSource(
    private val apiInterface: ApiInterface,
    private val query: String
) : PagingSource<Int, PixabayPhoto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PixabayPhoto> {
        val position = params.key ?: PIXABAY_STARTING_PAGE

        return try {

            val response =
                apiInterface.searchPixabayPhotos(
                    PIXABAY_API_KEY,
                    query,
                    position,
                    params.loadSize
                )

            val photos = response.hits

            LoadResult.Page(
                data = photos,
                prevKey = if (position == PIXABAY_STARTING_PAGE) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )


        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }

    }


    override fun getRefreshKey(state: PagingState<Int, PixabayPhoto>): Int? {
        TODO("Not yet implemented")
    }

}