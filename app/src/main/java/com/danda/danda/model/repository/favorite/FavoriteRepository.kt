package com.danda.danda.model.repository.favorite

import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.util.Result

interface FavoriteRepository {
    suspend fun favoriteList(emailUser: String, result: (Result<List<Favorite>>) -> Unit)
}