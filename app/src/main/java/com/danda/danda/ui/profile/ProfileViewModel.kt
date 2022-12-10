package com.danda.danda.ui.profile

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
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _getUser = MutableLiveData<Result<FirebaseUser?>>()
    val getUser: LiveData<Result<FirebaseUser?>> = _getUser

    private val _getFromUser = MutableLiveData<Result<User?>>()
    val getFromUser: LiveData<Result<User?>> = _getFromUser

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
    fun getUserFromFireStore(email:String){
        viewModelScope.launch {
            profileRepository.getProfileUser(email){
                _getFromUser.value = it
            }
        }
    }

    fun logout() {
        userRepository.logout {}
    }

}