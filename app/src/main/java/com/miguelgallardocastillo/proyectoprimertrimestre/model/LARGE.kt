package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LARGE(
    val height: Int,
    val url: String,
    val width: Int
): Parcelable