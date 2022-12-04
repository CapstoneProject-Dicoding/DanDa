package com.danda.danda.model.repository.home

import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Result

interface HomeRepository {
    suspend fun homeList(result: (Result<List<Recipe>>) -> Unit)
    suspend fun searchHomeList(nameRecipe: String?, result: (Result<List<Recipe>?>) -> Unit)
}