package com.danda.danda.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.repository.banner.BannerRepository
import com.danda.danda.model.repository.home.HomeRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val bannerRepository: BannerRepository,
    private val homeRepository: HomeRepository
    ) : ViewModel() {

    private val _banner = MutableLiveData<Result<List<String>>>()
    val banner: LiveData<Result<List<String>>>
        get() = _banner

    private val _listRecipe = MutableLiveData<Result<List<Recipe>?>>()
    val recipe: LiveData<Result<List<Recipe>?>>
        get() = _listRecipe

    fun getBanner() {
        viewModelScope.launch {
            bannerRepository.banner {
                _banner.value = it
            }
        }
    }

    fun getListRecipe() {
        _listRecipe.value = Result.Loading
        viewModelScope.launch {
            homeRepository.homeList {
                _listRecipe.value = it
            }
        }
    }

    fun searchListRecipe(nameRecipe: String?) {
        _listRecipe.value = Result.Loading
        viewModelScope.launch {
            homeRepository.searchHomeList(nameRecipe) {
                _listRecipe.value = it
            }
        }
    }
}