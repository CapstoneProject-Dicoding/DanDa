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

    private val _registerUser = MutableLiveData<Result<String>>()
    val registerUser: LiveData<Result<String>>
            get() = _registerUser

    fun registerUser(user: User) {
        _registerUser.value = Result.Loading
        userRepository.registerUser(user) {
            _registerUser.value = it
        }
    }

}