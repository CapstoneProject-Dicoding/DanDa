package com.danda.danda.model.repository.profile

import android.net.Uri
import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private var auth: FirebaseAuth,
    private val fireStoreUser : FirebaseFirestore
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

    override suspend fun getProfileUser(email: String?, result: (Result<User>) -> Unit) {
        fireStoreUser.collection(Constants.USER)
            .whereEqualTo("email",email)
            .get()
            .addOnSuccessListener {
                try {
                    for(document in it){
                        val data = document.toObject(User::class.java)
                        result.invoke(Result.Success(data))
                    }

                }catch (e:Exception){
                    result.invoke(Result.Failure("fail to get data"))
                }
            }
    }

}