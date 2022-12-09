package com.miguelgallardocastillo.proyectoprimertrimestre.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TotalNutrients(
    val CA: CA,
    @SerializedName("CHOCDF")
    val chocdf: CHOCDF,
    @SerializedName("CHOCDF.net")
    val CHOCDF_net: CHOCDFNet,
    val CHOLE: CHOLE,
    val ENERC_KCAL: ENERCKCAL,
    val FAMS: FAMS,
    val FAPU: FAPU,
    val FASAT: FASAT,
    val FAT: FAT,
    val FATRN: FATRN,
    val FE: FE,
    val FIBTG: FIBTG,
    val FOLAC: FOLAC,
    val FOLDFE: FOLDFE,
    val FOLFD: FOLFD,
    val K: K,
    val MG: MG,
    val NA: NA,
    val NIA: NIA,
    val P: P,
    val PROCNT: PROCNT,
    val RIBF: RIBF,
    val SUGAR: SUGAR,
    @SerializedName("SUGAR.added")
    val SUGAR_added: SUGARAdded,
    @SerializedName("Sugar.alcohol")
    val Sugar_alcohol: SugarAlcohol,
    val THIA: THIA,
    val TOCPHA: TOCPHA,
    val VITA_RAE: VITARAE,
    val VITB12: VITB12,
    val VITB6A: VITB6A,
    val VITC: VITC,
    val VITD: VITD,
    val VITK1: VITK1,
    val WATER: WATER,
    val ZN: ZN
) : Parcelable