package com.danda.danda.ui.editprofile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danda.danda.model.dataclass.User
import com.danda.danda.model.repository.profile.ProfileRepository
import com.danda.danda.model.repository.user.UserRepository
import com.danda.danda.util.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
):ViewModel() {

    private val _updateResponse = MutableLiveData<Result<String?>>()
    val updateResponse: LiveData<Result<String?>> = _updateResponse

    private val _getFromUser = MutableLiveData<Result<User?>>()
    val getFromUser: LiveData<Result<User?>> = _getFromUser

    private val _getUser = MutableLiveData<Result<FirebaseUser?>>()
    val getUser: LiveData<Result<FirebaseUser?>> = _getUser


    fun updateImage(email: String, file: Uri){
        _updateResponse.value = Result.Loading
        viewModelScope.launch {
            profileRepository.updatePhoto(email,file){
                _updateResponse.value = it
            }

        }
    }
    fun updateName(name: String){
        _updateResponse.value = Result.Loading
        viewModelScope.launch {
            profileRepository.updateName(name){
                _updateResponse.value = it
            }
        }
    }
    fun updateUserFireStore(username: String, name: String, id: String,){
        _updateResponse.value = Result.Loading
        viewModelScope.launch {
            delay(5000)
            profileRepository.updateProfileUser(username,name,id){
                _updateResponse.value = it
            }
        }


    }

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

    fun getDataFromUser(email:String?){
        viewModelScope.launch {
            profileRepository.getProfileUser(email){
                _getFromUser.value = it
            }
        }
    }
}