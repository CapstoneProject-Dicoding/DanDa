package com.danda.danda.model.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe (
        var id: String = "",
        val nameRecipe: String = "",
        val ingredients: String = "",
        val tools: String = "",
        val howToCook: String = "",
        var imgUrl: String = ""
) : Parcelable