package com.danda.danda.model.repository.addrecipe

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.addrecipe.AddRecipeFragment
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject

class AddRecipeRepositoryImp @Inject constructor(private val databaseFirebase: FirebaseFirestore, private val databaseStorage: FirebaseStorage) : AddRecipeRepository {
    override suspend fun addRecipe(recipe: Recipe, result: (Result<String>) -> Unit) {
        val document = databaseFirebase.collection(Constants.RECIPE).document()
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
            .addOnSuccessListener {
                databaseStorage.reference.child("images/$nameRecipe")
                    .downloadUrl
                    .addOnSuccessListener {
                        if (it != null) Constants.DATA_URL_IMAGE = it.toString()

                        Log.d("TAG", "ini image nya $it")
                        Log.d("TAG", "ini image nya ${Constants.DATA_URL_IMAGE}")
                    }
            }

            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }

    }


    private fun downloadurl(name: String) {
//        val aa = databaseStorage.reference.child("images/$name").downloadUrl
//            .addOnSuccessListener {
//                Log.e("TAG", "success ${Constants.DATA_URL_IMAGE}}")
//            }

    }

//    override suspend fun addImageRecipe(file: String, result: (Result<String>) -> Unit) {
//        databaseStorage.reference.child("images/$file")
//            .downloadUrl
//            .addOnSuccessListener {
//                Log.d("TAG", "success $it")
//            }
//    }


}