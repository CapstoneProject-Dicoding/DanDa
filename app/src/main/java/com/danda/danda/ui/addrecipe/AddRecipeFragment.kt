package com.danda.danda.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.danda.danda.databinding.FragmentAddRecipeBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {

    private lateinit var addRecipeViewModel: AddRecipeViewModel
    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!

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

//    private val launcherIntentCamera = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
//        if (it.resultCode == RESULT_OK) {
//            val imageBitmap = it.data?.extras?.get("data") as Bitmap
//            binding.photoFood.setImageBitmap(imageBitmap)
//        }
//    }

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

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}