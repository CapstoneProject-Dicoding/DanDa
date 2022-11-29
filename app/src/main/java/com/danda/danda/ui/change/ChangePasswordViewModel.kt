package com.danda.danda.ui.change

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
class ChangePasswordViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    private val _changePassword = MutableLiveData<Result<String>>()
    val changePassword: LiveData<Result<String>>
        get() = _changePassword

    fun changePassword(password: String) {
        _changePassword.value = Result.Loading
        viewModelScope.launch {
            delay(2000)
            userRepository.changePassword(password) {
                _changePassword.value = it
            }
        }
    }
}