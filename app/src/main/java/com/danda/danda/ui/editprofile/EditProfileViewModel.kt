package com.danda.danda.ui.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.repository.profile.ProfileRepository
import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
):ViewModel() {

    private val _updateResponse = MutableLiveData<Result<String?>>()
    val updateResponse: LiveData<Result<String?>> = _updateResponse

    private val _getUser = MutableLiveData<Result<FirebaseUser?>>()
    val getUser: LiveData<Result<FirebaseUser?>> = _getUser

//    fun updateProfile(displayName:String,urlPhoto:String){
//        viewModelScope.launch {
//            profileRepository.editProfile(urlPhoto,displayName){
//                _updateResponse.value = it
//            }
//        }
//    }

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