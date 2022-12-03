package com.danda.danda.model.repository.profile

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseUser

interface ProfileRepository {
    suspend fun getProfile(result :  (Result<FirebaseUser?>) -> Unit)
    suspend fun editProfile(username : String,urlPhoto : String ,result : (Result<String?>)-> Unit)
}