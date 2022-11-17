package com.danda.danda.model.repository.home

import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class HomeRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore) : HomeRepository {
    override suspend fun homeList(result: (Result<List<String>>) -> Unit) {

    }
}