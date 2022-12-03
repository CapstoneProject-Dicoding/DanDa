package com.danda.danda.model.repository.profile

import android.net.Uri
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest

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

    override suspend fun editProfile(
        username: String,
        urlPhoto: String,
        result: (Result<String?>) -> Unit
    ) {
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser
        val profileUpdate = userProfileChangeRequest {
            displayName = username
            photoUri = Uri.parse(urlPhoto)
        }
        user!!.updateProfile(profileUpdate)
            .addOnCompleteListener{task->
                try {
                    if(task.isSuccessful){
                        result.invoke(Result.Success("update success"))
                    }

                }catch (e:Exception){
                    result.invoke(Result.Failure("update failed"))
                }
            }

    }

}