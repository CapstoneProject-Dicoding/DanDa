package com.danda.danda.model.repository.profile

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private var auth: FirebaseAuth
    ):ProfileRepository {
    override suspend fun getProfile(result: (Result<FirebaseUser?>) -> Unit) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        try {
            result.invoke(Result.Success(user))
        }catch (e:Exception){
            Result.Failure(e.message.toString())
        }
    }

}