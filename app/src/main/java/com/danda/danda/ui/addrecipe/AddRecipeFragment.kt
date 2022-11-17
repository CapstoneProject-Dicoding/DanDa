package com.danda.danda.ui.addrecipe

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.danda.danda.databinding.FragmentAddRecipeBinding
import com.danda.danda.util.createCustomTempFile
import com.danda.danda.util.uriToFile
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {

    private lateinit var addRecipeViewModel: AddRecipeViewModel
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

        setupViewModel()

        binding.btnTambahkan.setOnClickListener { addRecipe() }
//        binding.photoFood.setOnClickListener { openCamera() }
        takeAPicture()
    }

    private fun setupViewModel() {
        addRecipeViewModel = ViewModelProvider(this)[AddRecipeViewModel::class.java]
    }

    private fun addRecipe() {
        val nameRecipe = binding.etNamaResep.text.toString()
        val ingredients = binding.etBahan.text.toString()
        val tools = binding.etAlat.text.toString()
        val howToCook = binding.etTataCara.text.toString()

        when {
            nameRecipe.isEmpty() -> {
                binding.etNamaResep.error = "Harap masukkan nama resep terlebih dahulu"
            }
            ingredients.isEmpty() -> {
                binding.etBahan.error = "Harap masukkan bahan terlebih dahulu"
            }
            tools.isEmpty() -> {
                binding.etAlat.error = "Harap masukkan alat masak terlebih dahulu"
            }
            howToCook.isEmpty() -> {
                binding.etTataCara.error = "Harap masukkan tata cara masak terlebih dahulu"
            }
            else -> {
                addRecipeViewModel.saveRecipeFireStore(nameRecipe, ingredients, tools, howToCook)

                addRecipeViewModel.addSuccess.observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let {
                        addSuccess()
                    }
                }

                addRecipeViewModel.isLoading.observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let { state ->
                        showLoading(state)
                    }
                }

                addRecipeViewModel.isFailed.observe(viewLifecycleOwner) {
                    it.getContentIfNotHandled()?.let {
                        isFailed()
                    }
                }

                uploadImage()
            }
        }
    }

    // HELP BANG
    // ini gw bingung camera yg di fragment, kalo yg di dicoding kan buat activity tuh
    // jadinya beda, kalo tau tambahin yak -(revansyah)

//    private fun openCamera() {
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        intent.resolveActivity()
//
//        createCustomTempFile().also {
//
//        }
//    }
//
//    private val launcherIntentCamera = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == RESULT_OK) {
//            val imageBitmap = it.data?.extras?.get("data") as Bitmap
//            binding.photoFood.setImageBitmap(imageBitmap)
//        }
//    }


    // untuk mengambil image dari gallery atau camera bisa pake cara yg di bawah
    // silahkan dicoba untuk upload nya


    private fun uploadImage() {
        val nameRecipe = binding.etNamaResep.text.toString()

        val storage = FirebaseStorage.getInstance()
            .getReference("images/$nameRecipe")

        storage.putFile(getFile!!.toUri())
            .addOnCanceledListener {
                binding.photoFood.setImageURI(null)
                Toast.makeText(requireContext(), "berhasil", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "gagal", Toast.LENGTH_SHORT).show()
            }
    }


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


    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun addSuccess() {
        AlertDialog.Builder(requireContext()).apply {
            setCancelable(false)
            setTitle("Success")
            setMessage("Recipe Added Successfully")
            setPositiveButton("OK") { _, _ ->  }
            create()
            show()
        }
    }

    private fun isFailed() {
        AlertDialog.Builder(requireContext()).apply {
            setCancelable(false)
            setTitle("Failed")
            setMessage("Recipe Failed To Add")
            setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}