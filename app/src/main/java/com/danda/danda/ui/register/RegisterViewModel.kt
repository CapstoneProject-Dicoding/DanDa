package com.danda.danda.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.danda.danda.model.dataclass.User
import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {

    private val _addUser = MutableLiveData<Result<String>>()
    val addUser: LiveData<Result<String>>
            get() = _addUser

    fun addUser(user: User){
        _addUser.value = Result.Loading
        userRepository.addUser(user) {
            _addUser.value = it
        }
    }
}