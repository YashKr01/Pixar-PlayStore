package com.example.pixar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashPhotoUrls,
    val links: UnsplashLinks,
    val user: UnsplashUser
) : Parcelable {

    @Parcelize
    data class UnsplashPhotoUrls(
        val regular: String,
        val small: String,
    ) : Parcelable

    @Parcelize
    data class UnsplashLinks(
        val download_location: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String
    ) : Parcelable

}