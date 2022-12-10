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
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private var auth: FirebaseAuth,
    private val fireStoreUser : FirebaseFirestore,
    private val databaseStorage: FirebaseStorage
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
        name: String,
        id: String,
        email: String,
        file: Uri,
        result: (Result<String?>) -> Unit
    ) {
        databaseStorage.getReference("user_profile/${email}")
            .putFile(file)
            .addOnSuccessListener {
                databaseStorage.reference.child("user_profile/${email}")
                    .downloadUrl
                    .addOnSuccessListener { url->
                        if (url!=null){
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)

                                fireStoreUser.collection(Constants.USER)
                                    .document(id)
                                    .update(mapOf(
                                        "username" to username,
                                        "name" to name,
                                        "imgProfile" to url.toString()
                                    ))

                                auth = FirebaseAuth.getInstance()
                                val user = auth.currentUser
                                val profileUpdate = userProfileChangeRequest {
                                    displayName = name
                                    photoUri = Uri.parse(url.toString())
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

    override suspend fun updateProfileUser(
        username: String,
        name: String,
        id: String,
        imgUrl: String,
        result: (Result<String>) -> Unit
    ) {
        fireStoreUser.collection(Constants.USER)
            .document(id)
            .update(mapOf(
                "username" to username,
                "name" to name,
                "imgProfile" to imgUrl
            ))
            .addOnSuccessListener {
                result.invoke(Result.Success("success"))
            }
            .addOnFailureListener {
                result.invoke(Result.Failure("failed"))
            }
    }

}