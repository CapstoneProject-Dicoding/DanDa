package com.danda.danda.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.repository.favorite.FavoriteRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository): ViewModel() {

    private val _listFavorite = MutableLiveData<Result<List<Favorite>>>()
    val listFavorite: LiveData<Result<List<Favorite>>>
        get() = _listFavorite

    fun getFavoriteList(emailUser: String) {
        _listFavorite.value = Result.Loading
        viewModelScope.launch {
            favoriteRepository.favoriteList(emailUser) {
                _listFavorite.value = it
            }
        }
    }
}