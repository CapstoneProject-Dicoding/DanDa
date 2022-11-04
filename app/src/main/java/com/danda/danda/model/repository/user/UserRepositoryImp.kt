package com.danda.danda.model.repository.user

import com.danda.danda.model.dataclass.User
import com.danda.danda.util.FireStoreTables
import com.danda.danda.util.Result
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class UserRepositoryImp(private val database: FirebaseFirestore): UserRepository {

    override fun registerUser(user: User, result: (Result<String>) -> Unit) {
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

    override fun loginUser(email: String, password: String, result: (Result<String>) -> Unit) {

//        database.collection(FireStoreTables.USER)
//            .whereEqualTo("nama", true)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    Log.d(TAG, "${document.id} => ${document.data}")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents: ", exception)
//            }


//        val docRef = database.collection(FireStoreTables.USER).document("SF")
//        docRef.get()
//            .addOnSuccessListener { document ->
//                if (document != null) {
//                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
//                } else {
//                    Log.d(TAG, "No such document")
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(TAG, "get failed with ", exception)
//            }
//
//        database.collection(FireStoreTables.USER)
//            .get()
//            .addOnCompleteListener {
//                result.invoke(
//                    Result.Success("Note has been created successfully")
//                )
//            }
//            .addOnFailureListener {
//                result.invoke(
//                    Result.Failure(
//                        it.localizedMessage?.toString()
//                    )
//                )
//            }



//            .addOnSuccessListener {
//                for (document in it) {
//                    val note = document.toObject(User::class.java)
//                    if (email == note.email && password == note.password){
//                        result.invoke(
//                            Result.Success("done")
//                        )
//                    } else {
//                        Result.Failure("error")
//                    }
//                }
//            }
//            .addOnFailureListener {
//                result.invoke(
//                    Result.Failure(
//                        it.localizedMessage?.toString()
//                    )
//                )
//            }
    }

}