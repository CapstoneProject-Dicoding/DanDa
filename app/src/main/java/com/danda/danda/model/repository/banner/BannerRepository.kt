package com.danda.danda.model.repository.banner

import com.danda.danda.model.dataclass.ImageSlider
import com.danda.danda.util.Result
import com.denzcoskun.imageslider.models.SlideModel

interface BannerRepository {
    suspend fun banner(result: (Result<List<ImageSlider>>) -> Unit)
}