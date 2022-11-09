package com.danda.danda.model.repository.user

import com.danda.danda.util.Result

interface UserRepository {
    suspend fun registerUser(email: String, password: String, result: (Result<String>) -> Unit)
    suspend fun loginUser(email: String, password: String, result: (Result<String>) -> Unit)
    fun logout(result: (Result<String>) -> Unit)
}