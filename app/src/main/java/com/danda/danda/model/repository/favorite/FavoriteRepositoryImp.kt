package com.danda.danda.model.repository.favorite

import com.danda.danda.model.dataclass.Favorite
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

    override suspend fun getFavorite(
        emailUser: String?,
        nameRecipe: String,
        result: (Result<List<Favorite>>) -> Unit
    ) {
        databaseFirestore.collection(Constants.FAVORITE)
            .whereEqualTo("nameRecipe", nameRecipe)
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
    }

    override suspend fun addFavorite(fav: Favorite, result: (Result<String>) -> Unit) {
        val document = databaseFirestore.collection(Constants.FAVORITE).document()
        fav.id = document.id
        document.set(fav)
            .addOnSuccessListener {
                result.invoke(
                    Result.Success("Sukses menambahkan favorite")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }
}