package com.danda.danda.model.repository.detail

import android.net.Uri
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.danda.danda.util.Result
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore, private val databaseStorage: FirebaseStorage) : DetailRepository {
    override suspend fun commentList(
        nameRecipe: String,
        result: (Result<List<Comment>>) -> Unit
    ) {
        databaseFirestore.collection(Constants.COMMENT)
            .whereEqualTo("nameRecipe", nameRecipe).limit(3)
            .get()
            .addOnSuccessListener {
                val listComment = arrayListOf<Comment>()
                for (document in it) {
                    val comment = document.toObject(Comment::class.java)
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

    override suspend fun commentListDetail(
        nameRecipe: String,
        result: (Result<List<Comment>>) -> Unit
    ) {
        databaseFirestore.collection(Constants.COMMENT)
            .whereEqualTo("nameRecipe", nameRecipe)
            .get()
            .addOnSuccessListener {
                val listComment = arrayListOf<Comment>()
                for (document in it) {
                    val comment = document.toObject(Comment::class.java)
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

    override suspend fun addComment(comment: Comment, result: (Result<String>) -> Unit) {
        val document = databaseFirestore.collection(Constants.COMMENT).document()
        comment.id = document.id
        document.set(comment)
            .addOnSuccessListener {
                result.invoke(
                    Result.Success("Success menambahkan ulasan")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }

//    override suspend fun addFavorite(favorite: Favorite, file: Uri, result: (Result<String>) -> Unit) {
//        databaseStorage.getReference("images/${favorite.nameRecipe}")
//            .getFile(file)
//            .addOnSuccessListener {
//                databaseStorage.reference.child("images/${favorite.nameRecipe}")
//                    .downloadUrl
//                    .addOnSuccessListener { url ->
//                        if (url != null) {
//                            CoroutineScope(Dispatchers.IO).launch {
//                                delay(2000)
//                                val document = databaseFirestore.collection(Constants.FAVORITE).document()
//                                favorite.id = document.id
//                                favorite.imgUrl = url.toString()
//                                document.set(favorite)
//                                    .addOnSuccessListener {
//                                        result.invoke(
//                                            Result.Success("Berhasil menambahkan ke favorite")
//                                        )
//                                    }
//                                    .addOnFailureListener {
//                                        result.invoke(
//                                            Result.Failure(it.localizedMessage)
//                                        )
//                                    }
//                            }
//                        }
//                    }
//                    .addOnFailureListener {
//                        result.invoke(
//                            Result.Failure(it.localizedMessage)
//                        )
//                    }
//            }
//            .addOnFailureListener {
//                result.invoke(
//                    Result.Failure(it.localizedMessage)
//                )
//            }
//    }
}