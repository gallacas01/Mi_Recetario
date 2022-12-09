package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VITC(
    val label: String,
    val quantity: Double,
    val unit: String
): Parcelable