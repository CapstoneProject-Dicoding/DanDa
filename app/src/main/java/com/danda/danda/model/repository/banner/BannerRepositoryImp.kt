package com.danda.danda.model.repository.banner

import com.danda.danda.util.Constants
import com.danda.danda.util.Result
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import javax.inject.Inject

class BannerRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore) : BannerRepository {
    override suspend fun banner(result: (Result<List<String>>) -> Unit) {
        databaseFirestore.collection(Constants.IMAGE_SLIDER).get()
            .addOnSuccessListener {
                val imageList = ArrayList<String>()
                for (document in it) {
                    val image = document.getString("url")
                    imageList.add(image!!)
                }
                result.invoke(
                    Result.Success(imageList)
                )
            }
            .addOnFailureListener {
                result.invoke(
                    Result.Failure(it.localizedMessage)
                )
            }
    }

}