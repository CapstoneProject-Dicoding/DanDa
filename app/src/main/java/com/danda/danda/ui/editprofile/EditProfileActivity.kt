package com.danda.danda.ui.editprofile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
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

    private fun getProfile(){
        viewModel.getUser.observe(this){
            when(it){
                is Result.Success ->{
                    binding.usernameEt.setText(it.data?.displayName.toString())
                }
                is Result.Failure ->{
                    binding.usernameEt.setText(it.error.toString())
                }else->{
                binding.usernameEt.setText("ggbang")
                }
            }
        }
    }


}