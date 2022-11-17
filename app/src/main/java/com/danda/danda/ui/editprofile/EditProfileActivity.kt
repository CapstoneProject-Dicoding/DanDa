package com.danda.danda.ui.editprofile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.danda.danda.R
import com.danda.danda.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.changeProfileBt.setOnClickListener {
            val user = auth.currentUser
            user?.let {
                val username = binding.usernameEt.text.toString()
                val name = binding.nameEt.text.toString()
                val photoURI = Uri.parse("android.resource://$packageName/${R.drawable.ic_food}")
                val profileUpdates = UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .setPhotoUri(photoURI)
                    .build()

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        user.updateProfile(profileUpdates).await()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@EditProfileActivity, "Successfully updated profile",
                                Toast.LENGTH_LONG).show()
                        }
                    } catch(e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
        }

    }
}