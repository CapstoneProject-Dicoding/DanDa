package com.danda.danda.model.repository.addrecipe

import android.net.Uri
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Result

interface AddRecipeRepository {
    suspend fun addRecipe(recipe: Recipe, result: (Result<String>) -> Unit)
    suspend fun addImageRecipe(name: String, file: Uri, result: (Result<String>) -> Unit)
}