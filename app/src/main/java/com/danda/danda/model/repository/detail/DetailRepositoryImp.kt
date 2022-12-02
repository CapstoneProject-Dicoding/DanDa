package com.danda.danda.model.repository.detail

import com.danda.danda.model.dataclass.Comment
import com.danda.danda.util.Constants
import com.google.firebase.firestore.FirebaseFirestore
import com.danda.danda.util.Result
import javax.inject.Inject

class DetailRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore) : DetailRepository {
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
}