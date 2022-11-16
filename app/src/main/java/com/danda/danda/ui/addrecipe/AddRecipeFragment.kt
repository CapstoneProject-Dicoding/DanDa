package com.danda.danda.ui.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.danda.danda.databinding.FragmentAddRecipeBinding
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddRecipeFragment : Fragment() {

    private lateinit var addRecipeViewModel: AddRecipeViewModel
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentAddRecipeBinding? = null
    private val binding get() = _binding!!
    private var success: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRecipeBinding.inflate(inflater, container, false)

//        val textView: TextView = binding.textDashboard
//        addRecipeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFirebase()
        setupViewModel()

        binding.btnTambahkan.setOnClickListener { addRecipe() }

    }

    private fun setupViewModel() {
        addRecipeViewModel = ViewModelProvider(this)[AddRecipeViewModel::class.java]
    }

    private fun initializeFirebase() {
        db = FirebaseFirestore.getInstance()
    }

    private fun addRecipe() {
        val nameRecipe = binding.etNamaResep.text.toString()
        val ingredients = binding.etBahan.text.toString()
        val tools = binding.etAlat.text.toString()
        val howToCook = binding.etTataCara.text.toString()

//        when {
//            nameRecipe.isEmpty() -> { binding.etNamaResep.error = "Masukkan nama resep" }
//            ingredients.isEmpty() -> { binding.etBahan.error = "Masukkan bahan" }
//            tools.isEmpty() -> { binding.etAlat.error = "Masukkan Alat Masak" }
//            howToCook.isEmpty() -> { binding.etTataCara.error = "Masukkan Tata Cara Masak"}
//        }

        saveRecipeFireStore(nameRecipe, ingredients, tools, howToCook)

//        binding.photoFood.setOnClickListener { openCamera() }
    }

    private fun saveRecipeFireStore(
        nameRecipe: String,
        ingredients: String,
        tools: String,
        howToCook: String
    ) {
        val recipe: MutableMap<String, Any> = HashMap()
        recipe["nameRecipe"] = nameRecipe
        recipe["ingredients"] = ingredients
        recipe["tools"] = tools
        recipe["howToCook"] = howToCook

        db.collection("recipe")
            .add(recipe)
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Recipe Added Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Recipe Failed To Add",
                    Toast.LENGTH_SHORT
                ).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}