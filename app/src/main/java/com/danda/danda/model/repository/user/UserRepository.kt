package com.danda.danda.model.repository.user

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Result

interface UserRepository {

    fun registerUser(user: User, result: (Result<String>) -> Unit)
    fun loginUser(email: String, password: String, result: (Result<String>) -> Unit)
}