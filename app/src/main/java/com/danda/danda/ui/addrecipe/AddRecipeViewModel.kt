package com.danda.danda.ui.addrecipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.model.repository.addrecipe.AddRecipeRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddRecipeViewModel @Inject constructor(private val addRecipeRepository: AddRecipeRepository): ViewModel() {
    private val _addRecipe = MutableLiveData<Result<String>>()
    val addRecipe: LiveData<Result<String>>
        get() = _addRecipe

    private val _addImageRecipe = MutableLiveData<Result<String>>()
    val addImageRecipe: LiveData<Result<String>>
        get() = _addRecipe

    fun addRecipe(recipe: Recipe) {
        _addRecipe.value = Result.Loading
        viewModelScope.launch {
            delay(2000)
            addRecipeRepository.addRecipe(recipe) {
                _addRecipe.value = it
            }
        }
    }

    fun addImageRecipe(nameRecipe: String, images: Uri) {
        _addImageRecipe.value = Result.Loading
        viewModelScope.launch {
            addRecipeRepository.addImageRecipe(nameRecipe, images) {
                _addImageRecipe.value = it
            }
        }
    }
}