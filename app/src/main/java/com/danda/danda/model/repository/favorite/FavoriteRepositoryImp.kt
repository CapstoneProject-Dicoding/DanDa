package com.danda.danda.model.repository.favorite

import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FavoriteRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore): FavoriteRepository {
    override suspend fun favoriteList (
        emailUser: String,
        result: (Result<List<Favorite>>) -> Unit
    ) {
        databaseFirestore.collection(Constants.FAVORITE)
            .whereEqualTo("emailUser", emailUser)
            .get()
            .addOnSuccessListener {
                val listFavorite = arrayListOf<Favorite>()
                for (document in it) {
                    val favorite = document.toObject(Favorite::class.java)
                    listFavorite.add(favorite)
                }
                result.invoke(
                    Result.Success(listFavorite)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }
}