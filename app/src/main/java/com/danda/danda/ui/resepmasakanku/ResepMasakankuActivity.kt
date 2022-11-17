package com.danda.danda.ui.resepmasakanku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.danda.danda.R
import com.danda.danda.databinding.ActivityResepMasakankuBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ResepMasakankuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResepMasakankuBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResepMasakankuBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

//    private fun initializeFireStore() {
//        db = Firebase.firestore
//    }
}