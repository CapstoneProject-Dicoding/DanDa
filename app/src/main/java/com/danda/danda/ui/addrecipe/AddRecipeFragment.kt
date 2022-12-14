package com.danda.danda.ui.addrecipe

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.danda.danda.MainActivity
import com.danda.danda.R
import com.danda.danda.databinding.FragmentAddRecipeBinding
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.editprofile.EditProfileViewModel
import com.danda.danda.ui.login.LoginActivity
import com.danda.danda.ui.profile.ProfileViewModel
import com.danda.danda.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.math.log

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {
    private val addRecipeViewModel by viewModels<AddRecipeViewModel>()
    private val profileViewModel by viewModels<ProfileViewModel>()
    private val getUser by viewModels<EditProfileViewModel>()
    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAction()
    }

    private fun setAction() {
        takeAPicture()
        checkStatus()
        checkUser()
    }

    private fun checkStatus() {
        addRecipeViewModel.addRecipe.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Result.Loading -> showLoading(true, binding.progressBarAddRecipe)
                is Result.Failure -> {
                    showLoading(false, binding.progressBarAddRecipe)
                    requireActivity().showToast(status.error.toString())
                }
                is Result.Success -> {
                    requireActivity().showToast(status.data)
                    showLoading(false, binding.progressBarAddRecipe)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                    requireActivity().finish()
                }
            }
        }
    }

    private fun checkUser() {
        profileViewModel.getUser.observe(viewLifecycleOwner) { status ->
            when(status) {
                is Result.Success -> {
                    if (status.data?.email == null && status.data?.displayName == null) {
                        loginHere()
                    } else {
                        getUser(status.data.email.toString())
                    }
                }
                else -> {}
            }
        }
    }

    private fun getUser(emailUser: String){
        getUser.getDataFromUser(emailUser)

        getUser.getFromUser.observe(viewLifecycleOwner) {
            when (it) {
                is Result.Success -> {
                    addRecipe(it.data?.email.toString(), it.data?.username.toString())
                }
                else -> {}
            }
        }
    }

    private fun addRecipe(emailUser: String, username: String) = binding.apply {
        btnTambahkan.setOnClickListener {
            val nameRecipe = etNamaResep.text.toString()
            val description = etDescription.text.toString()
            val ingredients = etBahan.text.toString()
            val howToCook = etTataCara.text.toString()

            if (getFile == null) {
                requireActivity().showToast("Masukkan Image terlebih dahulu")
            } else {
                if (nameRecipe.isEmpty()) {
                    requireActivity().showToast("Masukkan nama resep masakan  terlebih dahulu")
                } else if (ingredients.isEmpty()) {
                    requireActivity().showToast("Masukkan bahan masakan  terlebih dahulu")
                } else if (description.isEmpty()) {
                    requireActivity().showToast("Masukkan alat memasak terlebih dahulu")
                } else if (howToCook.isEmpty()) {
                    requireActivity().showToast("Masukkan tata cara memasak terlebih dahulu")
                } else {
                    addRecipeViewModel.addRecipe(
                        Recipe(
                            "",
                            nameRecipe,
                            ingredients,
                            description,
                            howToCook,
                            "",
                            emailUser,
                            username

                        ),
                        getFile!!.toUri()
                    )
                }
            }

            closedKeyboard()
        }
    }

    private fun loginHere() {
        AlertDialog.Builder(requireContext())
            .setTitle("KONFIRMASI STATUS")
            .setMessage("Jika kamu ingin menambahkan resep, anda harus login terlebih dahulu?")
            .setCancelable(false)

            .setPositiveButton("YA"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
            }

            .setNegativeButton("TIDAK"){ dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
                requireActivity().finish()
            }
            .show()
    }

    private fun takeAPicture() = binding.photoFood.setOnClickListener {
        when {
            !allPermissionsGranted() -> ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        AlertDialog.Builder(requireContext())
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
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        createCustomTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
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
                binding.photoFood.setImageBitmap(result)
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        when (result.resultCode) {
            RESULT_OK -> {
                val selectedImg: Uri = result.data?.data as Uri

                val myFile = uriToFile(selectedImg, requireContext())

                getFile = myFile

                binding.photoFood.setImageURI(selectedImg)
            }
        }
    }

    private fun closedKeyboard() {
        val view: View? = activity?.currentFocus
        val inputMethodManager: InputMethodManager
        when {
            view != null -> {
                inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val AUTHORITY = "com.danda.danda"
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}