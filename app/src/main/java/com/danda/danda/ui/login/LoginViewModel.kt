package com.danda.danda.ui.login

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
class LoginViewModel @Inject constructor(private val userRepository: UserRepository): ViewModel() {
    private val _loginUser = MutableLiveData<Result<String>>()
    val loginUser: LiveData<Result<String>>
        get() = _loginUser

    fun loginUser(email: String, password: String) {
        _loginUser.value = Result.Loading
        viewModelScope.launch {
            delay(2000)
            userRepository.loginUser(email, password) {
                _loginUser.value = it
            }
        }
    }
}