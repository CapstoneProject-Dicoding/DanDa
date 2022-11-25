package com.danda.danda.model.repository.user

import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImp @Inject constructor(private var auth: FirebaseAuth): UserRepository {

    override suspend fun registerUser(email: String, password: String, result: (Result<String>) -> Unit) {
        auth = FirebaseAuth.getInstance()
        try {
            val dataRegister = auth.createUserWithEmailAndPassword(email, password).await()
            result.invoke(
                Result.Success(dataRegister.toString())
            )
            Result.Success(dataRegister)
        } catch (e: Exception) {
            result.invoke(
                Result.Failure(e.message.toString())
            )
        }
    }

    override suspend fun loginUser(email: String, password: String, result: (Result<String>) -> Unit) {
        auth = FirebaseAuth.getInstance()
        try {
            val dataLogin = auth.signInWithEmailAndPassword(email, password).await()
            result.invoke(
                Result.Success(dataLogin.toString())
            )
            Result.Success(dataLogin)
        } catch (e: Exception) {
            result.invoke(
                Result.Failure(e.message.toString())
            )
        }
    }

    override fun logout(result: (Result<String>) -> Unit) {
        auth = FirebaseAuth.getInstance()
        try {
            val user = auth.currentUser
            if (user != null) {
                auth.signOut()
            }
        } catch (e: Exception) {
            result.invoke(
                Result.Failure(e.message.toString())
            )
        }
    }
}