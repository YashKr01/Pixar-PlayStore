package com.example.pixar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashPhotoUrls,
) : Parcelable {

    @Parcelize
    data class UnsplashPhotoUrls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    ) : Parcelable

}