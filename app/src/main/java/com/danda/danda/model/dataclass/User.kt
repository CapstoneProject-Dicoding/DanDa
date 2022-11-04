package com.danda.danda.model.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User (
    var id: String = "",
    var nama: String,
    var username: String,
    var password: String,
    var email: String
): Parcelable