package com.danda.danda.model.repository.resepmasakanku

import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class ResepMasakankuImp @Inject constructor(private val databaseFirestore: FirebaseFirestore) : ResepMasakankuRepository {
    override suspend fun resepMasakankuList(
        emailUser: String,
        result: (Result<List<Recipe>>) -> Unit
    ) {
        databaseFirestore.collection(Constants.RECIPE)
            .whereEqualTo("emailUser", emailUser)
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

}