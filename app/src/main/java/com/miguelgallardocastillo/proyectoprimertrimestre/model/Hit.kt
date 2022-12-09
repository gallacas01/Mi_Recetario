package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hit(
    val _links: LinksX,
    val recipe: Recipe
): Parcelable