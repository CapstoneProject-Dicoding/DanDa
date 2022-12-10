package com.danda.danda.model.repository.addrecipe

import android.net.Uri
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.dataclass.User
import com.danda.danda.util.Result

interface AddRecipeRepository {
    suspend fun addRecipe(recipe: Recipe, file: Uri, result: (Result<String>) -> Unit)
}