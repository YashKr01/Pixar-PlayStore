package com.example.pixar.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PixabayPhoto(
    val id:Int,
    val webformatURL:String,
    val largeImageURL:String,
    val user:String
) : Parcelable
