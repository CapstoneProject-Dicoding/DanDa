package com.danda.danda.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.repository.profile.ProfileRepository
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text
    private val _getUser = MutableLiveData<Result<FirebaseUser?>>()
    val getUser: LiveData<Result<FirebaseUser?>> = _getUser

    init {
        getUserProfile()
    }
    private fun getUserProfile(){
        viewModelScope.launch {
            profileRepository.getProfile {
                _getUser.value = it
            }
        }
    }

}