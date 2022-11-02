package com.danda.danda.model.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var id: String = "",
    val nama: String,
    val username: String,
    val password: String,
    val email: String
): Parcelable