package com.danda.danda.model.repository.home

import com.danda.danda.util.Result

interface HomeRepository {
    suspend fun homeList(result: (Result<List<String>>) -> Unit)
}