package com.danda.danda.ui.addrecipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.dataclass.User
import com.danda.danda.model.repository.addrecipe.AddRecipeRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(private val addRecipeRepository: AddRecipeRepository): ViewModel() {

    private val _addRecipe = MutableLiveData<Result<String>>()
    val addRecipe: LiveData<Result<String>>
        get() = _addRecipe

    private val _getUser = MutableLiveData<Result<User?>>()
    val getUser: LiveData<Result<User?>>
        get() = _getUser

    fun addRecipe(recipe: Recipe, file: Uri) {
        _addRecipe.value = Result.Loading
        viewModelScope.launch {
            addRecipeRepository.addRecipe(recipe, file) {
                _addRecipe.value = it
            }
        }
    }
}