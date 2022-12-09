package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class REGULAR(
    val height: Int,
    val url: String,
    val width: Int
): Parcelable