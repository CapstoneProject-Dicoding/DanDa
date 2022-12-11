package com.danda.danda.model.repository.user

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(private val databaseFirebase: FirebaseFirestore, private var auth: FirebaseAuth): UserRepository {
    override suspend fun registerUser(email: String, password: String, result: (Result<String>) -> Unit) {
        try {
            val dataRegister = auth.createUserWithEmailAndPassword(email, password).await()
            result.invoke(
                Result.Success("Created User")
            )
            Result.Success(dataRegister)
        } catch (e: Exception) {
            result.invoke(
                Result.Failure(e.localizedMessage)
            )
        }
    }

    override suspend fun loginUser(email: String, password: String, result: (Result<String>) -> Unit) {
        try {
            val dataLogin = auth.signInWithEmailAndPassword(email, password).await()
            result.invoke(
                Result.Success("Login Success")
            )
            Result.Success(dataLogin)
        } catch (e: Exception) {
            result.invoke(
                Result.Failure(e.localizedMessage)
            )
        }
    }

    override suspend fun changePassword(newPassword: String, result: (Result<String>) -> Unit) {
        auth.currentUser!!.updatePassword(newPassword)
            .addOnCompleteListener {
                result.invoke(
                    Result.Success("Change Password Successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure("Error")
                )
            }
    }

    override suspend fun addUsername(user: User, result: (Result<String>) -> Unit) {
        val document = databaseFirebase.collection(Constants.USER).document()
        user.id = document.id
        document.set(user)
            .addOnSuccessListener {
                result.invoke(
                    Result.Success("Sukses menambahkan resep")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }

    override fun logout(result: (Result<String>) -> Unit) {
        try {
            val user = auth.currentUser
            if (user != null) {
                auth.signOut()
            }
        } catch (e: Exception) {
            result.invoke(
                Result.Failure(e.localizedMessage)
            )
        }
    }
}