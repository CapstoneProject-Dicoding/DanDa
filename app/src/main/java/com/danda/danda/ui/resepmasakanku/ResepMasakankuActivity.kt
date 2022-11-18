package com.danda.danda.ui.resepmasakanku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.danda.danda.databinding.ActivityResepMasakankuBinding
import com.danda.danda.model.dataclass.Recipe
import com.danda.danda.ui.resepmasakanku.adapter.ResepMasakankuAdapter
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResepMasakankuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResepMasakankuBinding
    private lateinit var recipeList: ArrayList<Recipe>
    private var db = Firebase.firestore
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResepMasakankuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeList = arrayListOf()
        binding.rvResepMasakanku.layoutManager = LinearLayoutManager(this)

        initializeFireStore()
        readFireStoreData()
        readFireStorageData()
    }

    private fun initializeFireStore() {
        db = FirebaseFirestore.getInstance()
    }

    private fun readFireStoreData() {
        db.collection("recipe")
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val recipe: Recipe? = data.toObject(Recipe::class.java)
                        if (recipe != null) {
                            recipeList.add(recipe)
                        }
                    }
                    binding.rvResepMasakanku.adapter = ResepMasakankuAdapter(recipeList)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            }
    }

    private fun readFireStorageData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("images/")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapShot in snapshot.children) {
                        val image = dataSnapShot.getValue(Recipe::class.java)
                        recipeList.add(image!!)
                    }
                    binding.rvResepMasakanku.adapter = ResepMasakankuAdapter(recipeList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ResepMasakankuActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}