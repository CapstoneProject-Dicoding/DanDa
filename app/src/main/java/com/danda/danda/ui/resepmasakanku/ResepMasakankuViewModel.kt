package com.danda.danda.ui.resepmasakanku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.repository.resepmasakanku.ResepMasakankuRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResepMasakankuViewModel @Inject constructor(private val resepMasakankuRepository: ResepMasakankuRepository) : ViewModel() {

    private val _listRecipe = MutableLiveData<Result<List<Recipe>>>()
    val recipe: LiveData<Result<List<Recipe>>>
        get() = _listRecipe

    fun getListRecipe(emailUser: String) {
        _listRecipe.value = Result.Loading
        viewModelScope.launch {
            resepMasakankuRepository.resepMasakankuList(emailUser) {
                _listRecipe.value = it
            }
        }
    }
}