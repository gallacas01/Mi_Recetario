package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Receta (val label: String="", val urlImage:String="", val calories:String="", val urlRecipe:String? = "", val fat:String="", val carbs:String="", val protein:String="", val weight:String="")
    :Parcelable{
}