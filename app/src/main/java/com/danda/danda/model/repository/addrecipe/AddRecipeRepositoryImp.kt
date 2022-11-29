package com.danda.danda.model.repository.addrecipe

import android.net.Uri
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddRecipeRepositoryImp @Inject constructor(private val databaseFirebase: FirebaseFirestore, private val databaseStorage: FirebaseStorage) : AddRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe, file: Uri, result: (Result<String>) -> Unit) {
        databaseStorage.getReference("images/${recipe.nameRecipe}")
            .putFile(file)
            .addOnSuccessListener {
                databaseStorage.reference.child("images/${recipe.nameRecipe}")
                    .downloadUrl
                    .addOnSuccessListener { url ->
                        if (url != null) {
                            CoroutineScope(Dispatchers.IO).launch {
                                delay(3000)
                                val document = databaseFirebase.collection(Constants.RECIPE).document()
                                recipe.id = document.id
                                recipe.imgUrl = url.toString()
                                document.set(recipe)
                                    .addOnSuccessListener {
                                        result.invoke(
                                            Result.Success("Sukses menambahkan resep")
                                        )
                                    }
                                    .addOnFailureListener {
                                        result.invoke(
                                            Result.Failure(it.localizedMessage)
                                        )
                                    }
                            }
                        }
                    }
                    .addOnFailureListener {
                        result.invoke(
                            Result.Failure(it.localizedMessage)
                        )
                    }
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }
}