package com.danda.danda.model.repository.user

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Result

interface UserRepository {

    fun addUser(user: User, result: (Result<String>) -> Unit)
}