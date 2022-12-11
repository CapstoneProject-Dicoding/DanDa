package com.danda.danda.ui.editprofile

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.danda.danda.MainActivity
import com.danda.danda.R
import com.danda.danda.databinding.ActivityEditProfileBinding
import com.danda.danda.util.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    lateinit var auth: FirebaseAuth
    private val viewModel by viewModels<EditProfileViewModel>()
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

       setAction()
    }

    private fun setAction() {
        getProfile()
        updateProfile()
        resultUpdateProfile()
        takeAPicture()
        back()

        binding.btnBack.setOnClickListener { onBackPressed() }
    }


    private fun updateProfile(){
        binding.changeProfileBt.setOnClickListener {
            val username = binding.usernameEt.text.toString()
            val name = binding.nameEt.text.toString()

            if (username.isEmpty()){
                showToast("masukkan username")
            }else if(name.isEmpty()){
                showToast("masukkan name dulu")
            }else{
                updateImage()
                updateName()
                updateUserFireStore()
            }

            Log.d("namenya ada ga", name)
        }
    }
    private fun updateImage(){
        if(getFile!=null){
            viewModel.updateImage(emailData,getFile!!.toUri())
        }
    }
    private fun updateName(){
        if(binding.nameEt.text.toString().isNotEmpty()){
            val name = binding.nameEt.text.toString()
            viewModel.updateName(name)
        }
    }
    private fun updateUserFireStore(){
        if(binding.usernameEt.text.toString().isNotEmpty()){
            val username = binding.usernameEt.text.toString()
            val name = binding.nameEt.text.toString()
            viewModel.updateUserFireStore(username,name, id)
        }
    }

    private fun resultUpdateProfile(){
        viewModel.updateResponse.observe(this){result->
            when(result){
                is Result.Success ->{
                    showToast("update success")
                    showLoading(false,binding.progressBarLogin)
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }
                is Result.Failure ->{
                    showToast("update failed")
                    showLoading(false,binding.progressBarLogin)
                }is Result.Loading->{
                showLoading(true,binding.progressBarLogin)
            }
            }
        }
    }

    private fun getProfile(){
        viewModel.getUser.observe(this){
            when(it){
                is Result.Success ->{
                    binding.nameEt.setText(it.data?.displayName.toString())
                    getProfileFromUser(it.data?.email)
                }
                is Result.Failure ->{
                    binding.nameEt.setText(it.error.toString())
                }
                is Result.Loading ->{

                }
            }
        }
    }
    private fun getProfileFromUser(email:String?){
        viewModel.getDataFromUser(email)
        viewModel.getFromUser.observe(this){
            when(it){
                is Result.Success ->{
                    binding.usernameEt.setText(it.data?.username)
                    id = it.data?.id.toString()
                    emailData= it.data?.email.toString()
                }
                is Result.Failure ->{
                    showToast(it.error.toString())
                }
                is Result.Loading ->{

                }
            }

        }
    }

    private fun takeAPicture() = binding.profileIv.setOnClickListener {
        when {
            !allPermissionsGranted() -> ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Ambil gambar dengan")

            .setPositiveButton("CAMERA"){ dialogInterface: DialogInterface, _: Int ->
                openCamera()
                dialogInterface.dismiss()
            }

            .setNegativeButton("GALLERY"){ dialogInterface: DialogInterface, _: Int ->
                openGallery()
                dialogInterface.dismiss()
            }
            .show()
    }

    private fun allPermissionsGranted() = arrayOf(Manifest.permission.CAMERA).all {
        ContextCompat.checkSelfPermission(
            this.baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(this.packageManager)

        createCustomTempFile(this).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                AUTHORITY,
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "pilih gambar")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            RESULT_OK -> {
                val myFile = File(currentPhotoPath)
                getFile = myFile

                val result = BitmapFactory.decodeFile(getFile?.path)
                binding.profileIv.setImageBitmap(result)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val selectedImg: Uri = result.data?.data as Uri

                val myFile = uriToFile(selectedImg, this)

                getFile = myFile

                binding.profileIv.setImageURI(selectedImg)
            }
        }
    }

    private fun back() {
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        getProfile()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    companion object{
        private var id = "id"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val AUTHORITY = "com.danda.danda"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private var emailData = "email"
    }


}