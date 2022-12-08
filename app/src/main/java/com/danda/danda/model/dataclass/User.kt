package com.danda.danda.model.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var id: String = "",
    val name: String = "",
    val username: String = "",
    val imgProfile: String = "",
    val email: String = ""
): Parcelable