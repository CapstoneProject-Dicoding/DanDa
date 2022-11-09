package com.danda.danda.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private val _registerUser = MutableLiveData<Result<String>>()
    val registerUser: LiveData<Result<String>>
            get() = _registerUser

    fun registerUser(email: String, password: String) {
        _registerUser.value = Result.Loading
        viewModelScope.launch {
            delay(2000)
            userRepository.registerUser(email, password) {
                _registerUser.value = it
            }
        }
    }
}