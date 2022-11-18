package com.danda.danda.model.repository.addrecipe

import android.net.Uri
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.FireStoreTables
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class AddRecipeRepositoryImp @Inject constructor(private val databaseFirebase: FirebaseFirestore, private val databaseStorage: FirebaseStorage) : AddRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe, result: (Result<String>) -> Unit) {
        val document = databaseFirebase.collection(FireStoreTables.RECIPE).document()
        recipe.id = document.id
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

    override suspend fun addImageRecipe(nameRecipe: String, file: Uri, result: (Result<String>) -> Unit) {
        databaseStorage.getReference("images/$nameRecipe")
            .putFile(file)
            .addOnCanceledListener {
                result.invoke(
                    Result.Success("Sukses menambahkan images")
                )
            }

            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }

}