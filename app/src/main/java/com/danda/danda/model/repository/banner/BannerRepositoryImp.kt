package com.danda.danda.model.repository.banner

import android.transition.Slide
import com.danda.danda.model.dataclass.ImageSlider
import com.danda.danda.util.FireStoreTables
import com.danda.danda.util.Result
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import javax.inject.Inject

class BannerRepositoryImp @Inject constructor(private val databaseFirestore: FirebaseFirestore) : BannerRepository {
//    firestore.collection("image_slider")
//    .get()
//    .addOnSuccessListener {
//        for (document in it) {
//            imageList.add(SlideModel(document.getString("url"), ScaleTypes.FIT))
//            binding.imageSliderHome.setImageList(
//                imageList
//            )
//        }
//    }
//    .addOnFailureListener {
//
//    }

    override suspend fun banner(result: (Result<List<ImageSlider>>) -> Unit) {
        databaseFirestore.collection(FireStoreTables.IMAGE_SLIDER).get()
            .addOnSuccessListener {
                val imageList = arrayListOf<ImageSlider>()
                for (document in it) {
                    val image = document.toObject(ImageSlider::class.java)
                    imageList.add(image)
//                    imageList.add(SlideModel(document.getString("url"), ScaleTypes.FIT))
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