package com.danda.danda.model.repository.user

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.FireStoreTables
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore

class UserRepositoryImp(private val database: FirebaseFirestore): UserRepository {

    override fun addUser(user: User, result: (Result<String>) -> Unit) {
        val document = database.collection(FireStoreTables.USER).document()
        user.id = document.id
        document
            .set(user)
            .addOnSuccessListener {
                result.invoke(
                    Result.Success("Note has been created successfully")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(
                        it.localizedMessage?.toString()
                    )
                )
            }
    }

}