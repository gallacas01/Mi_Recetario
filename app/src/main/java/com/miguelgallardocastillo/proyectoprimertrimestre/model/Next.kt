package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Next(
    val href: String,
    val title: String
): Parcelable