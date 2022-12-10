package com.danda.danda.ui.editprofile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.danda.danda.R
import com.danda.danda.databinding.ActivityEditProfileBinding
import com.danda.danda.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.danda.danda.util.Result
import com.danda.danda.util.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<EditProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getProfile()
        updateProfile()
        resultUpdateProfile()



//        auth = FirebaseAuth.getInstance()

//        binding.changeProfileBt.setOnClickListener {
//            val user = auth.currentUser
//            user?.let {
//                val username = binding.usernameEt.text.toString()
//                val name = binding.nameEt.text.toString()
//                val photoURI = Uri.parse("android.resource://$packageName/${R.drawable.ic_food}")
//                val profileUpdates = UserProfileChangeRequest.Builder()
//                    .setDisplayName(username)
//                    .setPhotoUri(photoURI)
//                    .build()
//
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        user.updateProfile(profileUpdates).await()
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(this@EditProfileActivity, "Successfully updated profile",
//                                Toast.LENGTH_LONG).show()
//                        }
//                    } catch(e: Exception) {
//                        withContext(Dispatchers.Main) {
//                            Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_LONG).show()
//                        }
//                    }
//
//                }
//            }
//        }

    }

    private fun updateProfile(){
        binding.changeProfileBt.setOnClickListener {
            val name = binding.usernameEt.text.toString()
            val url = "https://firebasestorage.googleapis.com/v0/b/dandarecipe.appspot.com/o/user_profile%2Fcapcut.jpeg?alt=media&token=438c2b21-82de-4083-840f-91e4e6f97637"

            viewModel.updateProfile(
                name,url
            )
            Log.d("namenya ada ga", name)
        }
    }

    private fun resultUpdateProfile(){
        viewModel.updateResponse.observe(this){result->
            when(result){
                is Result.Success ->{
                    showToast("update success")
                }
                is Result.Failure ->{
                    showToast("update failed")
                }else->{
                binding.usernameEt.setText("ggbang")
            }
            }
        }
    }

    private fun getProfile(){
        viewModel.getUser.observe(this){
            when(it){
                is Result.Success ->{
                    binding.usernameEt.setText(it.data?.displayName.toString())
                    Glide.with(this)
                        .load(it.data?.photoUrl)
                        .into(binding.profileIv)
                    getProfileFromUser(it.data?.email)

                }
                is Result.Failure ->{
                    binding.usernameEt.setText(it.error.toString())
                }else->{
                binding.usernameEt.setText("ggbang")
                }
            }
        }
    }
    private fun getProfileFromUser(email:String?){
        viewModel.getDataFromUser(email)
        viewModel.getFromUser.observe(this){
            when(it){
                is Result.Success ->{
                    binding.nameEt.setText(it.data?.name)
                }
                is Result.Failure ->{
                    showToast(it.error.toString())
                }else->{
                binding.usernameEt.setText("ggbang")
            }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        getProfile()
    }


}