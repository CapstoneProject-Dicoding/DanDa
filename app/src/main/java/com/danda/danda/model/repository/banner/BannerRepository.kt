package com.danda.danda.model.repository.banner

import com.danda.danda.util.Result

interface BannerRepository {
    suspend fun banner(result: (Result<List<String>>) -> Unit)
}