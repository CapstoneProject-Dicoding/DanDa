package com.danda.danda.model.repository.resepmasakanku

import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Result

interface ResepMasakankuRepository {
    suspend fun resepMasakankuList(emailUser: String, result: (Result<List<Recipe>>) -> Unit)
}