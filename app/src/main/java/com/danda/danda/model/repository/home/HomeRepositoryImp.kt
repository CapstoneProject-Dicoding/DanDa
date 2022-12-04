package com.danda.danda.model.repository.home

import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore) : HomeRepository {
    override suspend fun homeList(result: (Result<List<Recipe>>) -> Unit) {
        databaseFirestore.collection(Constants.RECIPE)
            .get()
            .addOnSuccessListener {
                val listRecipe = arrayListOf<Recipe>()
                    for (document in it) {
                        val recipe = document.toObject(Recipe::class.java)
                        Result
                        listRecipe.add(recipe)
                    }
                result.invoke(
                    Result.Success(listRecipe)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }

    override suspend fun searchHomeList(
        nameRecipe: String?,
        result: (Result<List<Recipe>?>) -> Unit
    ) {
        databaseFirestore.collection(Constants.RECIPE)
            .orderBy("nameRecipe").startAt(nameRecipe).endAt(nameRecipe + "\ufbff")
            .get()
            .addOnSuccessListener {
                val listComment = arrayListOf<Recipe>()
                for (document in it) {
                    val comment = document.toObject(Recipe::class.java)
                    listComment.add(comment)
                }
                result.invoke(
                    Result.Success(listComment)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }
}