package com.danda.danda.ui.addrecipe

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.danda.danda.MainActivity
import com.danda.danda.databinding.FragmentAddRecipeBinding
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {
    private val addRecipeViewModel by viewModels<AddRecipeViewModel>()
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

        takeAPicture()
        checkStatusUploadRecipe()
        checkStatusUploadImageRecipe()
        addRecipe()
    }

    private fun checkStatusUploadRecipe() {
        addRecipeViewModel.addRecipe.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Result.Loading -> {
                    showLoading(true, binding.progressBarAddRecipe)
                }
                is Result.Failure -> {
                    requireActivity().showToast(status.error.toString())
                    showLoading(false, binding.progressBarAddRecipe)
                }
                is Result.Success -> {
                    requireActivity().showToast(status.data)
                    showLoading(false, binding.progressBarAddRecipe)
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()
                }
            }
        }
    }

    private fun checkStatusUploadImageRecipe() {
        addRecipeViewModel.addImageRecipe.observe(viewLifecycleOwner) { status ->
            when (status) {
                is Result.Loading -> {}
                is Result.Failure -> requireActivity().showToast(status.error.toString())
                is Result.Success -> {}
            }
        }
    }

    private fun addRecipe() = binding.apply{
        btnTambahkan.setOnClickListener {
            val nameRecipe = etNamaResep.text.toString()
            val ingredients = etBahan.text.toString()
            val tools = etAlat.text.toString()
            val howToCook = etTataCara.text.toString()

            when {
                nameRecipe.isEmpty() -> {
                    requireActivity().showToast("Harap masukkan nama resep terlebih dahulu")
                }
                ingredients.isEmpty() -> {
                    requireActivity().showToast("Harap masukkan bahan terlebih dahulu")
                }
                tools.isEmpty() -> {
                    requireActivity().showToast("Harap masukkan alat masak terlebih dahulu")
                }
                howToCook.isEmpty() -> {
                    requireActivity().showToast("Harap masukkan tata cara masak terlebih dahulu")
                }
                else -> {
                    addRecipeViewModel.addRecipe(
                        Recipe(
                            "",
                            nameRecipe,
                            ingredients,
                            tools,
                            howToCook
                        )
                    )

                    addRecipeViewModel.addImageRecipe(
                        nameRecipe,
                        getFile!!.toUri()
                    )
                }
            }
        }
    }

//    private fun uploadImage(fileName: String) {
//        val storage = FirebaseStorage.getInstance()
//            .getReference("images/$fileName")
//
//        storage.putFile(getFile!!.toUri())
//            .addOnCanceledListener {
//                binding.photoFood.setImageURI(null)
//                Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener {
//                Toast.makeText(requireContext(), "gagal", Toast.LENGTH_SHORT).show()
//            }
//    }

    private fun takeAPicture() = binding.photoFood.setOnClickListener {
        when {
            !allPermissionsGranted() -> ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                10
            )
        }

        AlertDialog.Builder(requireContext())
            .setTitle("Konfirmasi")
            .setMessage("Ambil gambar dengan")

            .setPositiveButton("CAMERA"){ dialogInterface: DialogInterface, _: Int ->
                startTakePhoto()
                dialogInterface.dismiss()
            }

            .setNegativeButton("GALLERY"){ dialogInterface: DialogInterface, _: Int ->
                startGallery()
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

    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireContext().packageManager)

        createCustomTempFile(requireContext()).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.danda.danda",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private fun startGallery() {
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




//    private fun addSuccess() {
//        AlertDialog.Builder(requireContext()).apply {
//            setCancelable(false)
//            setTitle("Success")
//            setMessage("Recipe Added Successfully")
//            setPositiveButton("OK") { _, _ ->  }
//            create()
//            show()
//        }
//    }
//
//    private fun isFailed() {
//        AlertDialog.Builder(requireContext()).apply {
//            setCancelable(false)
//            setTitle("Failed")
//            setMessage("Recipe Failed To Add")
//            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
//            create()
//            show()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}