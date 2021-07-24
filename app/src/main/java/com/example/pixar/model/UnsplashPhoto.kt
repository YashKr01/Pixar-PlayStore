package com.example.pixar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UnsplashPhoto(
    val id: String,
    val urls: UnsplashPhotoUrls,
    val links: UnsplashLinks,
    val user: UnsplashUser,
    val likes: Int
) : Parcelable {

    @Parcelize
    data class UnsplashPhotoUrls(
        val regular: String,
        val small: String
    ) : Parcelable

    @Parcelize
    data class UnsplashLinks(
        val download_location: String
    ) : Parcelable

    @Parcelize
    data class UnsplashUser(
        val name: String,
        val username: String
    ) : Parcelable {
        val attributionUrl get() = "https://unsplash.com/$username?utm_source=Pixar&utm_medium=referral"
    }

}