package com.danda.danda.model.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageSlider(
    val url: String
): Parcelable
