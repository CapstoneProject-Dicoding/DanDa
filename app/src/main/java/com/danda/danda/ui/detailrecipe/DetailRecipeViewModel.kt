package com.danda.danda.ui.detailrecipe

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.Comment
import com.danda.danda.model.dataclass.Favorite
import com.danda.danda.model.repository.detail.DetailRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailRecipeViewModel @Inject constructor(private val detailRepository: DetailRepository) : ViewModel() {

    private val _listComment = MutableLiveData<Result<List<Comment>>>()
    val listComment: LiveData<Result<List<Comment>>>
        get() = _listComment

    private val _addComment = MutableLiveData<Result<String>>()
    val comment: LiveData<Result<String>>
        get() = _addComment

    fun getListComment(nameRecipe: String) {
        _listComment.value = Result.Loading
        viewModelScope.launch {
            detailRepository.commentList(nameRecipe) {
                _listComment.value = it
            }
        }
    }

    fun getCommentListDetail(nameRecipe: String) {
        _listComment.value = Result.Loading
        viewModelScope.launch {
            delay(2000)
            detailRepository.commentListDetail(nameRecipe) {
                _listComment.value = it
            }
        }
    }

    fun addComment(comment: Comment){
        _addComment.value = Result.Loading
        viewModelScope.launch {
            delay(2000)
            detailRepository.addComment(comment) {
                _addComment.value = it
            }
        }
    }
}