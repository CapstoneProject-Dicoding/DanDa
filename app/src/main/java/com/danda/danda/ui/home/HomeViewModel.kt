package com.danda.danda.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.ImageSlider
import com.danda.danda.model.repository.banner.BannerRepository
import com.danda.danda.util.Result
import com.denzcoskun.imageslider.models.SlideModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val bannerRepository: BannerRepository) : ViewModel() {
    private val _banner = MutableLiveData<Result<List<String>>>()
    val banner: LiveData<Result<List<String>>>
        get() = _banner

    fun getBanner() {
        viewModelScope.launch {
            bannerRepository.banner {
                _banner.value = it
            }
        }
    }
}